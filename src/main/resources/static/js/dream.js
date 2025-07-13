document.addEventListener('DOMContentLoaded', () => {
  const uncompletedTable = document.getElementById('uncompleted-dream-table');
  const completedTable = document.getElementById('completed-dream-table');

  // 新規夢ボタン
  document.querySelectorAll('#new-dream-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/dream-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ dream: '' })
      }).then(() => location.reload());
    });
  });

  // 削除ボタン
  document.querySelectorAll('.dream-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/dream-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => location.reload());
    });
  });

  // 完了・取消ボタン
  document.querySelectorAll('.dream-complete-button').forEach((btn) => {
    const row = btn.closest('tr');
    const comp = row ? row.querySelector('.dream-completed-input') : null;
    if (comp && comp.value) {
      btn.value = '取消';
    }
    btn.addEventListener('click', () => {
      if (!row || !comp) return;
      if (btn.value === '完了') {
        comp.value = new Date().toISOString().split('T')[0];
        btn.value = '取消';
        moveRow(row, true);
      } else {
        comp.value = '';
        btn.value = '完了';
        moveRow(row, false);
      }
      console.log(row);
      sendUpdate(row);
    });
  });

  // 入力変更
  document
    .querySelectorAll('.dream-input, .dream-completed-input')
    .forEach((el) => {
      el.addEventListener('change', () => {
        const row = el.closest('tr');
        if (row) sendUpdate(row);
      });
    });

  function moveRow(row, toCompleted) {
    if (!row) return;
    if (toCompleted) {
      if (completedTable && completedTable.tBodies.length > 0) {
        completedTable.tBodies[0].appendChild(row);
      }
    } else if (uncompletedTable && uncompletedTable.tBodies.length > 0) {
      uncompletedTable.tBodies[0].appendChild(row);
    }
  }

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      dream: row.querySelector('.dream-input').value,
      completedAt: row.querySelector('.dream-completed-input').value || null,
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    fetch('/dream-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }
});
