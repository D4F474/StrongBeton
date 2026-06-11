import { ImageSend } from './image-send';

describe('ImageSend', () => {
  it('should create an instance', () => {
    const file = new File(['avatar'], 'avatar.png', { type: 'image/png' });

    expect(new ImageSend('avatar', file)).toBeTruthy();
  });
});
