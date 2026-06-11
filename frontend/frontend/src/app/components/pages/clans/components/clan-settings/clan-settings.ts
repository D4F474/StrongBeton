import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ClanDto } from '../../../../../common/clan/clan-dto';
import { UpdateClanPayLoad } from '../../../../../common/clan/update-clan-pay-load';

@Component({
  selector: 'app-clan-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clan-settings.html',
  styleUrl: './clan-settings.scss',
})
export class ClanSettings implements OnChanges {
  @Input() clan: ClanDto | null = null;
  @Input() open = false;
  @Input() actionLoading = false;

  @Output() closeRequested = new EventEmitter<void>();
  @Output() updateClanRequested = new EventEmitter<UpdateClanPayLoad>();

  name = '';
  description = '';
  invite = false;

  ngOnChanges(): void {
    if (!this.clan) {
      return;
    }

    this.name = this.clan.name ?? '';
    this.description = this.clan.description ?? '';
    this.invite = this.clan.invite ?? this.clan.isInvite ?? false;
  }

  close(): void {
    if (this.actionLoading) {
      return;
    }

    this.closeRequested.emit();
  }

  save(): void {
    const name = this.name.trim();

    if (!name || this.actionLoading) {
      return;
    }

    this.updateClanRequested.emit({
      name,
      description: this.description.trim(),
      invite: this.invite,
    });
  }
}