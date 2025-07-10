document.addEventListener('DOMContentLoaded', () => {
  //入力欄を選択または、入力中に幅が自動で変わる
  enableFullTextDisplay('.word-input, .meaning-input,.example-input ');
  // 新規単語ボタンが押されたら
  document.querySelectorAll('#new-word-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      //WordRecordControllerにPOST
      fetch('/word-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ word: '', meaning: '', example: '' }),
      }).then(() => location.reload());
    });
  });

  // 削除ボタンが押されたら
  document.querySelectorAll('.word-delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr'); //削除ボタンが押された行を取得
      if (!row) return;
      const id = row.dataset.id; //その行のidを取得
      //WordRecordControllerにPOST
      fetch('/word-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }),
      }).then(() => location.reload());
    });
  });

  // ミスボタンが押されたらcountを+1する
  document.querySelectorAll('.word-mistake-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr'); // ボタンがある行を取得
      if (!row) return;
      const id = row.dataset.id; // その行のIDを取得
      // countの現在値を取得（input要素なのでvalueを使う）
      const countInput = row.querySelector('.count-input');
      if (!countInput) return;
      // 現在値を取得し、1加算して設定
      let currentCount = parseInt(countInput.value, 10) || 0;
      currentCount += 1;
      console.log(currentCount);
      countInput.value = currentCount;
      //countが変化したため、再度取得
      const rerow = btn.closest('tr');
      sendUpdate(rerow);
    });
  });

  //テキストに何か入力されたら
  document.querySelectorAll('.word-input, .meaning-input, .example-input, .count-input').forEach((el) => {
    el.addEventListener('input', () => {
      const row = el.closest('tr'); //入力された行を取得
      if (row) sendUpdate(row); //あれば更新
    });
  });

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

  // javaからhtml、htmlからjsにデータを取得用
  function gatherData(row) {
    return {
      id: parseInt(row.dataset.id, 10),
      word: row.querySelector('.word-input').value,
      meaning: row.querySelector('.meaning-input').value,
      example: row.querySelector('.example-input').value,
      count: row.querySelector('.count-input').value,
    };
  }
  // データベース更新用
  function sendUpdate(row) {
    const data = gatherData(row); //ここでフロント側のデータをすべて取得
    //WordRecordControllerにPOSTして、データベースに保存
    return fetch('/word-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
  }
});
