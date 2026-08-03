import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Summary, SummarizeRequest, DeleteResponse } from '../models/summary.model';

@Injectable({
  providedIn: 'root'
})
export class SummarizerService {
  //Local
  // private readonly API_BASE_URL = 'http://localhost:9207/api/summarizer';
// AWS
  // private readonly API_BASE_URL = 'http://textsummarizer.ap-south-1.elasticbeanstalk.com/api/summarizer';

  //Render
  private readonly API_BASE_URL = 'https://spring-ai-practice-project.onrender.com/api/summarizer';
  // Signals for reactive state management
  summaries = signal<Summary[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  /**
   * Health check endpoint
   */
  healthCheck(): Observable<string> {
    return this.http.get(`${this.API_BASE_URL}/health`, { responseType: 'text' })
      .pipe(catchError(this.handleError));
  }

  /**
   * Summarize text
   */
  summarizeText(text: string): Observable<Summary> {
    this.loading.set(true);
    this.error.set(null);

    const request: SummarizeRequest = { text };

    return this.http.post<Summary>(`${this.API_BASE_URL}/summarize`, request)
      .pipe(
        tap(() => this.loading.set(false)),
        catchError((error) => {
          this.loading.set(false);
          return this.handleError(error);
        })
      );
  }

  /**
   * Get all summaries
   */
  getAllSummaries(): Observable<Summary[]> {
    this.loading.set(true);
    this.error.set(null);

    return this.http.get<Summary[]>(`${this.API_BASE_URL}/summaries`)
      .pipe(
        tap(summaries => {
          this.summaries.set(summaries);
          this.loading.set(false);
        }),
        catchError((error) => {
          this.loading.set(false);
          return this.handleError(error);
        })
      );
  }

  /**
   * Get summary by ID
   */
  getSummaryById(id: number): Observable<Summary> {
    this.loading.set(true);
    this.error.set(null);

    return this.http.get<Summary>(`${this.API_BASE_URL}/summaries/${id}`)
      .pipe(
        tap(() => this.loading.set(false)),
        catchError((error) => {
          this.loading.set(false);
          return this.handleError(error);
        })
      );
  }

  /**
   * Delete summary by ID
   */
  deleteSummary(id: number): Observable<DeleteResponse> {
    this.loading.set(true);
    this.error.set(null);

    return this.http.delete<DeleteResponse>(`${this.API_BASE_URL}/summaries/${id}`)
      .pipe(
        tap(() => {
          // Remove from local state
          const updated = this.summaries().filter(s => s.id !== id);
          this.summaries.set(updated);
          this.loading.set(false);
        }),
        catchError((error) => {
          this.loading.set(false);
          return this.handleError(error);
        })
      );
  }

  /**
   * Handle HTTP errors
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = error.error?.message || `Error Code: ${error.status}\nMessage: ${error.message}`;
    }

    this.error.set(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
