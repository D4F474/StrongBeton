import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-post.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreatePostComponent {
  @Input() loading = false;
  @Output() create = new EventEmitter<string>();

  content = '';

  submit(): void {
    const trimmedContent = this.content.trim();

    if (!trimmedContent || this.loading) {
      return;
    }

    this.create.emit(trimmedContent);
    this.content = '';
  }
}