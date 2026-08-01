import { Injectable, signal } from '@angular/core';

export interface ModalConfig {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'danger' | 'warning' | 'info';
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  isOpen = signal<boolean>(false);
  config = signal<ModalConfig | null>(null);
  private resolvePromise?: (value: boolean) => void;

  confirm(config: ModalConfig): Promise<boolean> {
    this.config.set({
      confirmText: 'Confirm',
      cancelText: 'Cancel',
      type: 'info',
      ...config
    });
    this.isOpen.set(true);

    return new Promise<boolean>((resolve) => {
      this.resolvePromise = resolve;
    });
  }

  confirmAction(confirmed: boolean): void {
    this.isOpen.set(false);
    if (this.resolvePromise) {
      this.resolvePromise(confirmed);
      this.resolvePromise = undefined;
    }

    // Clear config after animation
    setTimeout(() => {
      this.config.set(null);
    }, 300);
  }
}
