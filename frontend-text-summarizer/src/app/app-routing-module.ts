import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SummarizerComponent } from './components/summarizer/summarizer.component';
import { SummariesListComponent } from './components/summaries-list/summaries-list.component';
import { SummaryDetailComponent } from './components/summary-detail/summary-detail.component';

const routes: Routes = [
  {
    path: '',
    component: SummarizerComponent,
    title: 'AI Text Summarizer - Home'
  },
  {
    path: 'summaries',
    component: SummariesListComponent,
    title: 'Summary History'
  },
  {
    path: 'summary/:id',
    component: SummaryDetailComponent,
    title: 'Summary Detail'
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
