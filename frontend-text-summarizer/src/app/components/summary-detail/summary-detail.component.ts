import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SummarizerService } from '../../services/summarizer.service';
import { ModalService } from '../../services/modal.service';
import { ToastService } from '../../services/toast.service';
import { Summary } from '../../models/summary.model';

@Component({
  selector: 'app-summary-detail',
  templateUrl: './summary-detail.component.html',
  standalone: false,
  styleUrl: './summary-detail.component.scss'
})
export class SummaryDetailComponent implements OnInit {
  summary = signal<Summary | null>(null);
  isLoading = signal<boolean>(false);
  error = signal<string | null>(null);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private summarizerService: SummarizerService,
    private modalService: ModalService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadSummary(parseInt(id, 10));
    }
  }

  /**
   * Load summary by ID
   */
  loadSummary(id: number): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.summarizerService.getSummaryById(id).subscribe({
      next: (summary) => {
        this.summary.set(summary);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set(err.message || 'Failed to load summary');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Delete the current summary
   */
  async deleteSummary(): Promise<void> {
    if (!this.summary()?.id) return;

    const confirmed = await this.modalService.confirm({
      title: 'Delete Summary',
      message: 'Are you sure you want to delete this summary? This action cannot be undone.',
      confirmText: 'Delete',
      cancelText: 'Cancel',
      type: 'danger'
    });

    if (!confirmed) {
      return;
    }

    this.summarizerService.deleteSummary(this.summary()!.id!).subscribe({
      next: () => {
        this.toastService.success('Summary deleted successfully');
        this.router.navigate(['/summaries']);
      },
      error: (err) => {
        this.toastService.error(err.message || 'Failed to delete summary');
      }
    });
  }

  /**
   * Copy text to clipboard
   */
  copyToClipboard(text: string, type: 'original' | 'summary'): void {
    navigator.clipboard.writeText(text).then(() => {
      const label = type === 'original' ? 'Original text' : 'Summary';
      this.toastService.success(`${label} copied to clipboard!`);
    }).catch(() => {
      this.toastService.error('Failed to copy to clipboard');
    });
  }

  /**
   * Format date for display
   */
  formatDate(dateString?: string): string {
    if (!dateString) return 'N/A';

    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Get compression ratio
   */
  getCompressionRatio(): number {
    const summary = this.summary();
    if (!summary || !summary.summarizedText) return 0;

    return (summary.summarizedText.length / summary.originalText.length) * 100;
  }

  /**
   * Get characters saved
   */
  getCharactersSaved(): number {
    const summary = this.summary();
    if (!summary || !summary.summarizedText) return 0;

    return summary.originalText.length - summary.summarizedText.length;
  }

  /**
   * Get word count
   */
  getWordCount(text: string): number {
    return text.trim().split(/\s+/).length;
  }

  /**
   * Download summary as text file
   */
  downloadSummary(): void {
    const summary = this.summary();
    if (!summary) return;

    try {
      const content = `
AI TEXT SUMMARIZER - Summary #${summary.id}
Generated on: ${this.formatDate(summary.createdAt)}
Model Used: ${summary.modelUsed}

==============================
SUMMARY
==============================

${summary.summarizedText}

==============================
ORIGINAL TEXT
==============================

${summary.originalText}

==============================
STATISTICS
==============================

Compression Ratio: ${this.getCompressionRatio().toFixed(2)}%
Characters Saved: ${this.getCharactersSaved()}
Original Word Count: ${this.getWordCount(summary.originalText)}
Summary Word Count: ${this.getWordCount(summary.summarizedText || '')}
      `.trim();

      const blob = new Blob([content], { type: 'text/plain' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `summary-${summary.id}.txt`;
      a.click();
      window.URL.revokeObjectURL(url);

      this.toastService.success('Summary downloaded successfully');
    } catch (error) {
      this.toastService.error('Failed to download summary');
    }
  }

  /**
   * Navigate back to list
   */
  goBack(): void {
    this.router.navigate(['/summaries']);
  }

  /**
   * Create new summary
   */
  createNew(): void {
    this.router.navigate(['/']);
  }
}
