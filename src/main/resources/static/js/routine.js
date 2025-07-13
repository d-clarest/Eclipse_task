document.addEventListener('DOMContentLoaded', () => {
  function initTimeSelects(hourSel, minuteSel, time) {
    for (let h = 0; h < 24; h++) {
      const opt = document.createElement('option');
      opt.value = String(h).padStart(2, '0');
      opt.textContent = opt.value;
      hourSel.appendChild(opt);
    }
    for (let m = 0; m < 60; m += 5) {
      const opt = document.createElement('option');
      opt.value = String(m).padStart(2, '0');
      opt.textContent = opt.value;
      minuteSel.appendChild(opt);
    }
    if (time) {
      const [h, mi] = time.split(':');
      hourSel.value = h;
      minuteSel.value = mi.padStart(2, '0');
    }
  }
  // 新規ルーティンボタン
  document.querySelectorAll('#new-routine-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/routine-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: '', type: '予定', frequency: '毎日', timing: '12:00' }),
      }).then(() => location.reload());
    });
  });

  // 削除ボタン
  document.querySelectorAll('.routine-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/routine-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
      }).then(() => location.reload());
    });
  });

  document.querySelectorAll('.routine-hour').forEach((sel) => {
    const minuteSel = sel.parentElement.querySelector('.routine-minute');
    initTimeSelects(sel, minuteSel, sel.dataset.time);
    sel.addEventListener('change', () => {
      const row = sel.closest('tr');
      if (row) sendUpdate(row);
    });
    if (minuteSel) {
      minuteSel.addEventListener('change', () => {
        const row = sel.closest('tr');
        if (row) sendUpdate(row);
      });
    }
  });

  // 入力変更
  document
    .querySelectorAll('.routine-name-input, .routine-type-select, .routine-frequency-select, .routine-hour, .routine-minute')
    .forEach((el) => {
      el.addEventListener('change', () => {
        const row = el.closest('tr');
        if (row) sendUpdate(row);
      });
    });

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      name: row.querySelector('.routine-name-input').value,
      type: row.querySelector('.routine-type-select').value,
      frequency: row.querySelector('.routine-frequency-select').value,
      timing:
        row.querySelector('.routine-hour').value.padStart(2, '0') +
        ':' +
        row.querySelector('.routine-minute').value.padStart(2, '0'),
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/routine-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
  }
});
