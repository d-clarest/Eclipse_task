document.addEventListener('DOMContentLoaded', () => {
  const uncompletedTable = document.getElementById('uncompleted-task-table');
  const completedTable = document.getElementById('completed-task-table');
  const pointDisplay = document.getElementById('total-point-display');
  enableFullTextDisplay('.task-title-input, .task-result-input, .task-detail-input');
  refreshTotalPoint();

  //タスク新規のボタンを押されたら、
  const newButton = document.getElementById('new-task-button');
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/task-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '', category: '今日', result: '', detail: '', level: 1 }),
        keepalive: true, //POSTしたらすぐリロードなら、つけておくと安全
      }).then(() => location.reload());
    });
  }

  //削除ボタンが押されたら
  document.querySelectorAll('.task-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/task-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
        keepalive: true,//POSTしたらすぐリロードなら、つけておくと安全
      }).then(() => location.reload());
    });
  });

  //完了ボタンか取消ボタンのどっちを表示するかというのと，完了ボタンが押されたときのイベントを
  document.querySelectorAll('.task-complete-button').forEach((btn) => {
    const row = btn.closest('tr');
    const comp = row ? row.querySelector('.task-completed-input') : null;
    //完了日が書いているなら既に完了しているので，取消ボタンになるよ
    if (comp && comp.value) {
      btn.value = '取消';
      moveRow(row, true);//完了済みタスクのとこにレコード追加
    }
    //完了ボタンが押されたら
    btn.addEventListener('click', () => {
      if (!row || !comp) return;
      //もし完了ボタンを押したら
      if (btn.value === '完了') {
        comp.value = new Date().toISOString().split('T')[0];//完了日を入れる
        btn.value = '取消';
        moveRow(row, true);//完了済みに移動させる
        const expiration=row ? row.querySelector('.task-completed-input') : null;
        const diffCell = row.cells[6];//期日を取得
        if (diffCell) diffCell.textContent = '';//期日をnull
        sortAllTaskTables();//締切が速い順に並び替え
      } else {
        comp.value = '';
        btn.value = '完了';
        moveRow(row, false);//未完了に移動させる
        updateTimeUntilDue(row);//期日を再計算，期日は区分に依存している
        sortAllTaskTables();//締切が速い順に
      }
      sendUpdate(row).then(refreshTotalPoint);//サーバーサイドのデータベース更新＆ポイント更新
    });
  });

    //インプット欄に何か入力されたらサーバーへデータ更新
  ['.task-title-input', '.task-result-input', '.task-detail-input', '.task-level-select', '.task-completed-input', '.task-category-select'].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      const handler = () => {
        const row = inp.closest('tr');
        if (!row) return;
        if (selector === '.task-category-select') {
          updateTimeUntilDue(row);
          console.log('a');
          sortAllTaskTables();
        }
        sendUpdate(row).then(refreshTotalPoint);
      };
      inp.addEventListener('change', handler);
      inp.addEventListener('input', handler);
    });
  });

  //各データベースのすべてのポイントを取得（task-top.htmlで表示用）
  function refreshTotalPoint() {
    if (!pointDisplay) return;
    fetch('/total-point')
      .then((res) => res.json())
      .then((pt) => {
        pointDisplay.textContent = `${pt}P`;
      });
  }

  //完了済みのテーブルに入れるか，未完了のテーブルに入れるかを決定する
  function moveRow(row, completed) {
    if (!row) return;
    //タスクボックスの中なら
    if (uncompletedTable && completedTable) {
      const target = completed ? completedTable : uncompletedTable;
      const tbody = target.tBodies[0] || target;
      tbody.appendChild(row);
    }
  }

  // javaからhtml、htmlからjsにデータを取得用
  function gatherData(row) {
    //.valueをつける関数
    const val = (sel) => {
      const el = row.querySelector(sel);
      return el ? el.value : '';
    };
    const lvl = row.querySelector('.task-level-select');
    const lvlValue = lvl ? parseInt(lvl.value, 10) : 1;
    return {
      id: parseInt(row.dataset.id, 10),
      title: val('.task-title-input'),
      category: val('.task-category-select'),
      result: val('.task-result-input'),
      detail: val('.task-detail-input'),
      level: lvlValue,
      completedAt: val('.task-completed-input') || null,
    };
  }

  // サーバ側のデータベース更新用
  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/task-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
      keepalive: true,
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
    const expired = diff <= 0;
    if (diff < 0) diff = 0;
    diff = Math.floor(diff / 5) * 5;
    const days = Math.floor(diff / (60 * 24));
    const hours = Math.floor((diff % (60 * 24)) / 60);
    const mins = diff % 60;
    const deadlineCell = row.cells[5];
    if (deadlineCell) {
      const y = deadline.getFullYear();
      const m = String(deadline.getMonth() + 1).padStart(2, '0');
      const d = String(deadline.getDate()).padStart(2, '0');
      const hh = String(deadline.getHours()).padStart(2, '0');
      const mm = String(deadline.getMinutes()).padStart(2, '0');
      deadlineCell.textContent = `${y}-${m}-${d} ${hh}:${mm}`;
    }
    const diffCell = row.cells[6];
    if (diffCell) {
      if (expired) {
        diffCell.textContent = '期限切れ';
        diffCell.style.color = 'red';
      } else {
        diffCell.textContent = `${days}日${hours}時間${mins}分`;
        diffCell.style.color = '';
      }
    }
  }

  function sortTaskTable(table) {
    if (!table) return;
    const tbody = table.tBodies[0] || table;
    const rows = Array.from(tbody.querySelectorAll('tr.task-row'));
    // parse deadline text like "2024-06-07 14:30"; if invalid, place at the end
    const parseDeadline = (row) => {
      const cell = row.cells[5];
      if (!cell) return Number.POSITIVE_INFINITY;
      const text = cell.textContent.trim();
      const time = Date.parse(text.replace(/-/g, '/')); // Safari compatibility
      return isNaN(time) ? Number.POSITIVE_INFINITY : time;
    };
    rows.sort((a, b) => parseDeadline(a) - parseDeadline(b));
    rows.forEach((r) => tbody.appendChild(r));
  }

  //テーブルを締切の早い順にソート
  function sortAllTaskTables() {
    //task-topのやつも，タスクボックスにあるやつも適用
    document.querySelectorAll('.task-database').forEach(sortTaskTable);
  }

  //入力欄を選択または、入力中に幅が自動で変わる
  function enableFullTextDisplay(selector) {
    document.querySelectorAll(selector).forEach((inp) => {
      let originalWidth = ''; //フォーカスが外れたときにこの幅に戻す
      //入力欄の幅を内容に合わせて調整する関数
      const adjustWidth = () => {
        inp.style.width = 'auto'; //幅を自動に設定し、幅の計算をリセット
        const w = inp.scrollWidth + 4; //「4px」の余裕を足して、文字が切れないように
        inp.style.width = w + 'px';
      };
      // フォーカスが当たった瞬間に、現在の幅を originalWidth に保存
      inp.addEventListener('focus', () => {
        originalWidth = inp.style.width || inp.offsetWidth + 'px'; //inp.style.width が設定されていればそれを使う.なければ、現在の見た目の幅である offsetWidth を使う。
        adjustWidth(); //幅を内容に合わせて拡げる
      });
      inp.addEventListener('input', adjustWidth); //ユーザーが文字を入力するたびに adjustWidth を呼び、入力内容に合わせて幅を動的に変更
      // フォーカスが外れたら、保存しておいた元の幅に戻す
      inp.addEventListener('blur', () => {
        inp.style.width = originalWidth;
      });
    });
  }
});
