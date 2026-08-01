import { Component } from '@angular/core';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  standalone: false,
  styleUrl: './toast.component.scss'
})
export class ToastComponent {
  constructor(public toastService: ToastService) {}

  removeToast(id: number): void {
    this.toastService.remove(id);
  }

  getIcon(type: string): string {
    switch (type) {
      case 'success': return '✓';
      case 'error': return '✕';
      case 'warning': return '⚠';
      case 'info': return 'ℹ';
      default: return 'ℹ';
    }
  }
}
