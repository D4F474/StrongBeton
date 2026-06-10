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
export class ClanSettings {
  
}