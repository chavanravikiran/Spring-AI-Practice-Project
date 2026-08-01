import { Component, OnInit, signal } from '@angular/core';
import { SummarizerService } from '../../services/summarizer.service';
import { ToastService } from '../../services/toast.service';
import { Summary } from '../../models/summary.model';

export type HealthStatus = 'checking' | 'connected' | 'disconnected';

@Component({
  selector: 'app-summarizer',
  templateUrl: './summarizer.component.html',
  standalone: false,
  styleUrl: './summarizer.component.scss'
})
export class SummarizerComponent implements OnInit {
  inputText = signal<string>('');
  currentSummary = signal<Summary | null>(null);
  isLoading = signal<boolean>(false);
  error = signal<string | null>(null);
  success = signal<boolean>(false);
  healthStatus = signal<HealthStatus>('checking');

  // Sample texts for quick testing
  sampleTexts = [
    {
      label: 'AI & Technology',
      text: 'Artificial intelligence is transforming the way we interact with technology. Machine learning algorithms can now process vast amounts of data and make predictions with remarkable accuracy. This technology is being applied in various fields including healthcare, finance, and transportation.'
    },
    {
      label: 'Climate Change',
      text: 'Climate change is one of the most pressing issues of our time. Rising global temperatures are causing ice caps to melt, sea levels to rise, and extreme weather events to become more frequent. Scientists warn that without immediate action, the consequences could be catastrophic.'
    },
    {
      label: 'Spring Boot',
      text: 'Spring Boot is an open-source Java framework used to create microservices. It is developed by Pivotal Team and is used to build stand-alone and production-ready spring applications. Spring Boot provides a good platform for Java developers to develop a stand-alone and production-grade spring application that you can just run.'
    }
  ];

  constructor(
    public summarizerService: SummarizerService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.checkHealth();
  }

  /**
   * Check API health status
   */
  checkHealth(): void {
    this.healthStatus.set('checking');

    this.summarizerService.healthCheck().subscribe({
      next: (response) => {
        this.healthStatus.set('connected');
        console.log('API Health Check:', response);
      },
      error: (err) => {
        this.healthStatus.set('disconnected');
        console.error('API Health Check Failed:', err);
      }
    });
  }

  /**
   * Load a sample text
   */
  loadSampleText(text: string): void {
    this.inputText.set(text);
    this.currentSummary.set(null);
    this.error.set(null);
    this.success.set(false);
  }

  /**
   * Clear the form
   */
  clearForm(): void {
    this.inputText.set('');
    this.currentSummary.set(null);
    this.error.set(null);
    this.success.set(false);
  }

  /**
   * Summarize the input text
   */
  summarize(): void {
    const text = this.inputText().trim();

    if (!text) {
      this.error.set('Please enter some text to summarize');
      return;
    }

    this.isLoading.set(true);
    this.error.set(null);
    this.success.set(false);

    this.summarizerService.summarizeText(text).subscribe({
      next: (summary) => {
        this.currentSummary.set(summary);
        this.isLoading.set(false);
        this.success.set(true);

        // Clear success message after 3 seconds
        setTimeout(() => this.success.set(false), 3000);
      },
      error: (err) => {
        this.error.set(err.message || 'Failed to summarize text');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Get character count
   */
  getCharacterCount(): number {
    return this.inputText().length;
  }

  /**
   * Get word count
   */
  getWordCount(): number {
    const text = this.inputText().trim();
    return text ? text.split(/\s+/).length : 0;
  }

  /**
   * Copy summarized text to clipboard
   */
  copySummary(): void {
    const summary = this.currentSummary()?.summarizedText;
    if (summary) {
      navigator.clipboard.writeText(summary).then(() => {
        this.toastService.success('Summary copied to clipboard!');
      }).catch(() => {
        this.toastService.error('Failed to copy to clipboard');
      });
    }
  }
}
