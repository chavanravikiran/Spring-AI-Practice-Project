import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { SummarizerService } from '../../services/summarizer.service';
import { ModalService } from '../../services/modal.service';
import { ToastService } from '../../services/toast.service';
import { Summary } from '../../models/summary.model';

@Component({
  selector: 'app-summaries-list',
  templateUrl: './summaries-list.component.html',
  standalone: false,
  styleUrl: './summaries-list.component.scss'
})
export class SummariesListComponent implements OnInit {
  summaries = signal<Summary[]>([]);
  filteredSummaries = signal<Summary[]>([]);
  isLoading = signal<boolean>(false);
  error = signal<string | null>(null);
  searchTerm = signal<string>('');
  sortBy = signal<'date' | 'length'>('date');
  sortOrder = signal<'asc' | 'desc'>('desc');

  constructor(
    public summarizerService: SummarizerService,
    private router: Router,
    private modalService: ModalService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadSummaries();
  }

  /**
   * Load all summaries from the API
   */
  loadSummaries(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.summarizerService.getAllSummaries().subscribe({
      next: (summaries) => {
        this.summaries.set(summaries);
        this.applyFiltersAndSort();
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set(err.message || 'Failed to load summaries');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Delete a summary
   */
  async deleteSummary(id: number, event: Event): Promise<void> {
    event.stopPropagation();

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

    this.summarizerService.deleteSummary(id).subscribe({
      next: () => {
        this.summaries.set(this.summaries().filter(s => s.id !== id));
        this.applyFiltersAndSort();
        this.toastService.success('Summary deleted successfully');
      },
      error: (err) => {
        this.toastService.error(err.message || 'Failed to delete summary');
      }
    });
  }

  /**
   * View summary details
   */
  viewSummary(id: number): void {
    this.router.navigate(['/summary', id]);
  }

  /**
   * Search summaries
   */
  onSearchChange(term: string): void {
    this.searchTerm.set(term);
    this.applyFiltersAndSort();
  }

  /**
   * Change sort criteria
   */
  changeSortBy(sortBy: 'date' | 'length'): void {
    if (this.sortBy() === sortBy) {
      // Toggle order if same sort criteria
      this.sortOrder.set(this.sortOrder() === 'asc' ? 'desc' : 'asc');
    } else {
      this.sortBy.set(sortBy);
      this.sortOrder.set('desc');
    }
    this.applyFiltersAndSort();
  }

  /**
   * Apply filters and sorting
   */
  private applyFiltersAndSort(): void {
    let filtered = [...this.summaries()];

    // Apply search filter
    const term = this.searchTerm().toLowerCase();
    if (term) {
      filtered = filtered.filter(s =>
        s.originalText.toLowerCase().includes(term) ||
        s.summarizedText?.toLowerCase().includes(term)
      );
    }

    // Apply sorting
    filtered.sort((a, b) => {
      let comparison = 0;

      if (this.sortBy() === 'date') {
        const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
        const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
        comparison = dateA - dateB;
      } else {
        comparison = a.originalText.length - b.originalText.length;
      }

      return this.sortOrder() === 'asc' ? comparison : -comparison;
    });

    this.filteredSummaries.set(filtered);
  }

  /**
   * Get preview text (truncated)
   */
  getPreviewText(text: string, maxLength: number = 150): string {
    return text.length > maxLength
      ? text.substring(0, maxLength) + '...'
      : text;
  }

  /**
   * Format date for display
   */
  formatDate(dateString?: string): string {
    if (!dateString) return 'N/A';

    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return `${diffMins} min${diffMins > 1 ? 's' : ''} ago`;
    if (diffHours < 24) return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    if (diffDays < 7) return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`;

    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  /**
   * Copy summary to clipboard
   */
  copySummary(summary: Summary, event: Event): void {
    event.stopPropagation();

    if (summary.summarizedText) {
      navigator.clipboard.writeText(summary.summarizedText).then(() => {
        this.toastService.success('Summary copied to clipboard!');
      }).catch(() => {
        this.toastService.error('Failed to copy to clipboard');
      });
    }
  }

  /**
   * Navigate to create new summary
   */
  createNew(): void {
    this.router.navigate(['/']);
  }
}
