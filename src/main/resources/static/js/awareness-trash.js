document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.awareness-restore-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/awareness-restore', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => location.reload());
    });
  });

  document.querySelectorAll('tr[data-expiry]').forEach((row) => {
    const cell = row.querySelector('.time-left');
    const expiry = parseInt(row.dataset.expiry, 10);
    const update = () => {
      const diff = expiry - Date.now();
      if (diff <= 0) {
        cell.textContent = '0:00';
        return;
      }
      const minutes = Math.floor(diff / 60000);
      const seconds = Math.floor((diff % 60000) / 1000);
      cell.textContent = `${minutes}:${String(seconds).padStart(2, '0')}`;
    };
    update();
    setInterval(update, 1000);
  });
});
