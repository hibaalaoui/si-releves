import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Modal from './Modal';

describe('Modal', () => {
  it('ne devrait pas rendre le modal quand isOpen est false', () => {
    render(
      <Modal isOpen={false} onClose={vi.fn()} title="Test Modal">
        <div>Content</div>
      </Modal>
    );

    expect(screen.queryByText('Test Modal')).not.toBeInTheDocument();
  });

  it('devrait rendre le modal quand isOpen est true', () => {
    render(
      <Modal isOpen={true} onClose={vi.fn()} title="Test Modal">
        <div>Content</div>
      </Modal>
    );

    expect(screen.getByText('Test Modal')).toBeInTheDocument();
    expect(screen.getByText('Content')).toBeInTheDocument();
  });

  it('devrait appeler onClose quand on clique sur le bouton de fermeture', async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    render(
      <Modal isOpen={true} onClose={onClose} title="Test Modal">
        <div>Content</div>
      </Modal>
    );

    const closeButton = screen.getByRole('button');
    await user.click(closeButton);

    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it('devrait appeler onClose quand on clique sur l\'overlay', async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    const { container } = render(
      <Modal isOpen={true} onClose={onClose} title="Test Modal">
        <div>Content</div>
      </Modal>
    );

    const overlay = container.querySelector('.fixed.inset-0.bg-black');
    await user.click(overlay);

    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it('ne devrait pas appeler onClose quand on clique sur le contenu du modal', async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    render(
      <Modal isOpen={true} onClose={onClose} title="Test Modal">
        <div>Content</div>
      </Modal>
    );

    const content = screen.getByText('Content');
    await user.click(content);

    expect(onClose).not.toHaveBeenCalled();
  });

  it('devrait appliquer la classe de taille correcte', () => {
    const { container } = render(
      <Modal isOpen={true} onClose={vi.fn()} title="Test Modal" size="lg">
        <div>Content</div>
      </Modal>
    );

    const modal = container.querySelector('.max-w-2xl');
    expect(modal).toBeInTheDocument();
  });
});
