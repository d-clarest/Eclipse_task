document.addEventListener('DOMContentLoaded', () => {
  const pointDisplay = document.getElementById('total-point-display');
  enableFullTextDisplay('.awareness-input');//入力欄を選択または、入力中に幅が自動で変わる
  refreshTotalPoint();

  //新規気づきボタンが押されたら
  document.querySelectorAll('#new-awareness-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      fetch('/awareness-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ awareness: '', awarenessLevel: 1 })
      }).then(() => location.reload());
    });
  });

  //削除ボタンが押されたら
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

  //テキストに何か入力されたら
  document.querySelectorAll('.awareness-input, .awareness-level-select').forEach((el) => {
    el.addEventListener('change', () => {
      const row = el.closest('tr');
      if (row) sendUpdate(row);
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
      adjustWidth(); // 初期表示時にも現在の文字数に合わせて幅を調整
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

  // javaからhtml、htmlからjsにデータを取得用
  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      awareness: row.querySelector('.awareness-input').value,
      awarenessLevel: parseInt(row.querySelector('.awareness-level-select').value, 10)
    };
  }

  // サーバ側のデータベース更新用
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
});
