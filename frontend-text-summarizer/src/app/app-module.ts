import { NgModule, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { SummarizerComponent } from './components/summarizer/summarizer.component';
import { SummariesListComponent } from './components/summaries-list/summaries-list.component';
import { SummaryDetailComponent } from './components/summary-detail/summary-detail.component';
import { ModalComponent } from './components/modal/modal.component';
import { ToastComponent } from './components/toast/toast.component';

@NgModule({
  declarations: [
    App,
    SummarizerComponent,
    SummariesListComponent,
    SummaryDetailComponent,
    ModalComponent,
    ToastComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideHttpClient(withFetch())
  ],
  bootstrap: [App]
})
export class AppModule { }
