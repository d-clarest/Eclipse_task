//ページが全部読み込まれたら、中の処理を始める
document.addEventListener('DOMContentLoaded', () => {
  const unchallengedTable = document.getElementById('unchallenged-table');
  const completedTable = document.getElementById('completed-challenge-table');
  const pointDisplay = document.getElementById('total-point-display');
  const newButton = document.getElementById('new-challenge-button');
  document.querySelectorAll('.challenge-suc-button').forEach(addSuccessListener);
  document.querySelectorAll('.challenge-fail-button').forEach(addFailListener);
  document.querySelectorAll('.challenge-cancel-button').forEach(addCancelListener);
  refreshTotalPoint();
  enableFullTextDisplay('.challenge-title-input, .challenge-risk-input, .challenge-expected-input, .challenge-strategy-input, .challenge-actual-input, .challenge-improvement-input');


  //挑戦ボタンが押されたら
  if (newButton) {
    newButton.addEventListener('click', () => {
      fetch('/challenge-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: '' }),
      }).then(() => {
        location.reload();
      });
    });
  }

  //削除ボタンが押されたら
  document.querySelectorAll('.challenge-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id;
      fetch('/challenge-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
      }).then(() => {
        location.reload();
      });
    });
  });

  //テキストに何か入力されたら
  [
    '.challenge-title-input',
    '.challenge-risk-input',
    '.challenge-expected-input',
    '.challenge-strategy-input',
    '.challenge-actual-input',
    '.challenge-improvement-input',
  ].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      inp.addEventListener('change', () => {
        const row = inp.closest('tr');
        if (row) sendUpdate(row);
      });
    });
  });

  //成功ボタンが押されたら
   function addSuccessListener(btn) {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const actual = row.querySelector('.challenge-actual-input');
      if (actual) actual.value = '（成功）' + actual.value;
      const date = row.querySelector('.challenge-date-input');
      if (date) {
        const now = new Date();
        const yyyy = now.getFullYear();
        const mm = String(now.getMonth() + 1).padStart(2, '0');
        const dd = String(now.getDate()).padStart(2, '0');
        date.value = `${yyyy}-${mm}-${dd}`;
      }
      sendUpdate(row).then(refreshTotalPoint);//サーバサイドのデータベースを更新後，ポイント更新
      moveRow(row, true);//表面上のテーブル移動
      replaceWithCancel(row);//取消ボタンに変更され，それが押されたときのイベントの設定
    });
  }

  //失敗ボタンが押されたら
  function addFailListener(btn) {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const actual = row.querySelector('.challenge-actual-input');
      if (actual) actual.value = '（失敗）' + actual.value;
      const date = row.querySelector('.challenge-date-input');
      if (date) {
        const now = new Date();
        const yyyy = now.getFullYear();
        const mm = String(now.getMonth() + 1).padStart(2, '0');
        const dd = String(now.getDate()).padStart(2, '0');
        date.value = `${yyyy}-${mm}-${dd}`;
      }
      sendUpdate(row).then(refreshTotalPoint);
      moveRow(row, true);
      replaceWithCancel(row);
    });
  }

  //取消ボタンが押されたら
  function addCancelListener(btn) {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const actual = row.querySelector('.challenge-actual-input');
      if (actual) {
        //成功または失敗を削除,それ以外の文字は残す
        actual.value = actual.value
          .replace(/^（成功）/, '')
          .replace(/^（失敗）/, '')
          .replace(/^\(成功\)/, '')
          .replace(/^\(失敗\)/, '');
      }
      const date = row.querySelector('.challenge-date-input');
      if (date) date.value = '';//挑戦日をnullに
      sendUpdate(row).then(refreshTotalPoint);//サーバサイドのデータベースを更新後，ポイント更新
      moveRow(row, false);///表面上のテーブル移動
      replaceWithSucFail(row);
    });
  }

  //完了済みのテーブルに入れるか，未完了のテーブルに入れるかを決定する
  function moveRow(row, toCompleted) {
    if (!row) return;
    const target = toCompleted ? completedTable : unchallengedTable;
    if (target) {
      const tbody = target.tBodies[0] || target;
      tbody.appendChild(row);
    } else {
      row.style.display = toCompleted ? 'none' : '';//task-top画面で完了済みが押されたら非表示にするために，未完了なら表示にする
    }
  }

  //表面上のデータの吸い上げ
  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      title: row.querySelector('.challenge-title-input').value,
      risk: row.querySelector('.challenge-risk-input').value,
      expectedResult: row.querySelector('.challenge-expected-input').value,
      strategy: row.querySelector('.challenge-strategy-input').value,
      actualResult: row.querySelector('.challenge-actual-input').value,
      improvementPlan: row.querySelector('.challenge-improvement-input').value,
      challengeLevel: parseInt(row.querySelector('.challenge-level-input').value, 10),
      challengeDate: row.querySelector('.challenge-date-input').value || null,
    };
  }

  //サーバーサイドの更新
  function sendUpdate(row) {
    const data = gatherData(row);
    return fetch('/challenge-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
  }

  //成功・失敗ボタンを取消ボタンに変更
  function replaceWithCancel(row) {
    const cell = row.querySelector('td:first-child');
    if (!cell) return;
    cell.innerHTML = '<input type="button" value="取消" class="challenge-cancel-button" />';//HTMLの変更
    const btn = cell.querySelector('.challenge-cancel-button');
    if (btn) addCancelListener(btn);
  }

  //取消ボタンを成功・失敗ボタンに変更
  function replaceWithSucFail(row) {
    const cell = row.querySelector('td:first-child');
    if (!cell) return;
    cell.innerHTML = '<input type="button" value="成功" class="challenge-suc-button" /> <input type="button" value="失敗" class="challenge-fail-button" />';//HTMLの変更
    cell.querySelectorAll('.challenge-suc-button').forEach(addSuccessListener);
    cell.querySelectorAll('.challenge-fail-button').forEach(addFailListener);
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

  //各データベースのすべてのポイントを取得（task-top.htmlで表示用）
  function refreshTotalPoint() {
    if (!pointDisplay) return;
    fetch('/total-point')
      .then((res) => res.json())
      .then((pt) => {
        pointDisplay.textContent = `${pt}P`;
      });
  }
});
