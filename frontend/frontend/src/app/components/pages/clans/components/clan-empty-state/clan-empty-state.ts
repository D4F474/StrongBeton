import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CreateClanPayload } from '../../../../../common/clan/create-clan-payload';

@Component({
  selector: 'app-clan-empty-state',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clan-empty-state.html',
  styleUrl: './clan-empty-state.scss',
})
export class ClanEmptyState {
  @Input() actionLoading = false;

  @Output() createClanRequested = new EventEmitter<CreateClanPayload>();
  @Output() joinClanRequested = new EventEmitter<number>();

  createName = '';
  createDescription = '';
  createInviteOnly = false;

  joinClanId: number | null = null;

  createClan(): void {
    const name = this.createName.trim();

    if (!name || this.actionLoading) {
      return;
    }

    this.createClanRequested.emit({
      name,
      description: this.createDescription.trim(),
      invite: this.createInviteOnly,
    });
  }

  joinClan(): void {
    if (!this.joinClanId || this.actionLoading) {
      return;
    }

    this.joinClanRequested.emit(this.joinClanId);
  }
}