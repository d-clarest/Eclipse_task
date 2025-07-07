document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('#new-awareness-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/awareness-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ awareness: '', opinion: '', awarenessLevel: 1 })
      }).then(() => location.reload());
    });
  });

  const pointDisplay = document.getElementById('total-point-display');

  function refreshTotalPoint() {
    if (!pointDisplay) return;
    fetch('/total-point')
      .then((res) => res.json())
      .then((pt) => {
        pointDisplay.textContent = `${pt}P`;
      });
  }


  function enableFullTextDisplay(selector) {
    document.querySelectorAll(selector).forEach((inp) => {
      let originalWidth = '';
      const adjustWidth = () => {
        inp.style.width = 'auto';
        const w = inp.scrollWidth + 4; // some padding
        inp.style.width = w + 'px';
      };
      inp.addEventListener('focus', () => {
        originalWidth = inp.style.width || inp.offsetWidth + 'px';
        adjustWidth();
      });
      inp.addEventListener('input', adjustWidth);
      inp.addEventListener('blur', () => {
        inp.style.width = originalWidth;
      });
    });
  }

  enableFullTextDisplay('.awareness-input, .opinion-input');

  refreshTotalPoint();

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      awareness: row.querySelector('.awareness-input').value,
      opinion: row.querySelector('.opinion-input').value,
      awarenessLevel: parseInt(row.querySelector('.awareness-level-select').value, 10)
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/awareness-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(() => {
      refreshTotalPoint();
    });
  }

  document.querySelectorAll('.awareness-input, .opinion-input, .awareness-level-select').forEach((el) => {
    el.addEventListener('change', () => {
      const row = el.closest('tr');
      if (row) sendUpdate(row);
    });
    el.addEventListener('input', () => {
      const row = el.closest('tr');
      if (row) sendUpdate(row);
    });
  });

  document.querySelectorAll('.awareness-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/awareness-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => {
        location.reload();
      });
    });
  });
});
