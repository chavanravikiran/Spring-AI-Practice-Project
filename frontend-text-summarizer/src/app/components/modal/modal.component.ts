import { Component } from '@angular/core';
import { ModalService } from '../../services/modal.service';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  standalone: false,
  styleUrl: './modal.component.scss'
})
export class ModalComponent {
  constructor(public modalService: ModalService) {}

  onConfirm(): void {
    this.modalService.confirmAction(true);
  }

  onCancel(): void {
    this.modalService.confirmAction(false);
  }

  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.onCancel();
    }
  }
}
