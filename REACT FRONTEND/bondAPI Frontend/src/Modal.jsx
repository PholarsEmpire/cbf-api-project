// src/Modal.jsx
import { useEffect } from 'react';
import { createPortal } from 'react-dom';

export default function Modal({ onClose, children }) {
  useEffect(() => {
    const onKey = (e) => { if (e.key === 'Escape') onClose?.(); };
    document.addEventListener('keydown', onKey);
    document.body.style.overflow = 'hidden';
    return () => {
      document.removeEventListener('keydown', onKey);
      document.body.style.overflow = '';
    };
  }, [onClose]);

  return createPortal(
    <div
      style={styles.backdrop}
      onClick={onClose}
      aria-modal="true"
      role="dialog"
    >
      <div
        style={styles.modal}
        onClick={(e) => e.stopPropagation()}
      >
        {children}
      </div>
    </div>,
    document.body
  );
}

const styles = {
  backdrop: {
    position: 'fixed',
    inset: 0,
    background: 'rgba(0,0,0,0.6)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 9999,
    padding: '16px',
  },
  modal: {
    width: '100%',
    maxWidth: '720px',
    background: '#ffffff',
    color: '#0f172a',
    borderRadius: '12px',
    boxShadow: '0 20px 50px rgba(0,0,0,0.35)',
    padding: '24px',
    maxHeight: '90vh',
    overflow: 'auto',
  },
};
