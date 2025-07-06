document.addEventListener('DOMContentLoaded', () => {
  const newButton = document.getElementById('new-task-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/task-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '', category: '今日', result: '', detail: '', level: 1 })
      }).then(() => location.reload());
    });
  }

  const uncompletedTable = document.getElementById('uncompleted-task-table');
  const completedTable = document.getElementById('completed-task-table');

  function moveRow(row, completed) {
    if (!row) return;
    if (uncompletedTable && completedTable) {
      const target = completed ? completedTable : uncompletedTable;
      const tbody = target.tBodies[0] || target;
      tbody.appendChild(row);
    } else {
      row.style.display = completed ? 'none' : '';
    }
  }

  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      title: row.querySelector('.task-title-input').value,
      category: row.querySelector('.task-category-select').value,
      result: row.querySelector('.task-result-input').value,
      detail: row.querySelector('.task-detail-input').value,
      level: parseInt(row.querySelector('.task-level-select').value, 10),
      completedAt: row.querySelector('.task-completed-input').value || null
    };
  }

  function sendUpdate(row) {
    const data = gatherData(row);
    fetch('/task-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
  }

  function updateTimeUntilDue(row) {
    const sel = row.querySelector('.task-category-select');
    if (!sel) return;
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const sunday = new Date(today);
    sunday.setDate(today.getDate() + ((7 - today.getDay()) % 7));
    let deadline = new Date(today);
    switch (sel.value) {
      case '今日':
        deadline.setDate(today.getDate() + 1);
        break;
      case '明日':
        deadline.setDate(today.getDate() + 2);
        break;
      case '今週':
        deadline = new Date(sunday);
        deadline.setDate(deadline.getDate() + 1);
        break;
      case '来週':
        deadline = new Date(sunday);
        deadline.setDate(deadline.getDate() + 8);
        break;
      case '再来週':
        deadline = new Date(sunday);
        deadline.setDate(deadline.getDate() + 15);
        break;
      default:
        deadline.setDate(today.getDate() + 1);
    }
    deadline.setHours(0, 0, 0, 0);
    let diff = Math.floor((deadline - now) / 60000);
    if (diff < 0) diff = 0;
    diff = Math.floor(diff / 5) * 5;
    const days = Math.floor(diff / (60 * 24));
    const hours = Math.floor((diff % (60 * 24)) / 60);
    const mins = diff % 60;
    const cell = row.cells[4];
    if (cell) cell.textContent = `${days}日${hours}時間${mins}分`;
  }

  ['.task-title-input', '.task-result-input', '.task-detail-input', '.task-level-select', '.task-completed-input', '.task-category-select'].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      const handler = () => {
        const row = inp.closest('tr');
        if (!row) return;
        if (selector === '.task-category-select') updateTimeUntilDue(row);
        sendUpdate(row);
      };
      inp.addEventListener('change', handler);
      inp.addEventListener('input', handler);
    });
  });

  document.querySelectorAll('.task-complete-button').forEach((btn) => {
    const row = btn.closest('tr');
    const comp = row ? row.querySelector('.task-completed-input') : null;
    if (comp && comp.value) {
      btn.value = '取消';
      moveRow(row, true);
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
      sendUpdate(row);
    });
  });

  document.querySelectorAll('.task-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/task-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) })
      }).then(() => {
        row.remove();
      });
    });
  });
});
