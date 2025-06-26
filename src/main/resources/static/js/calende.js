// Calendar with month navigation

document.addEventListener('DOMContentLoaded', () => {
  //ページが全部読み込まれたら、中の処理を始める
  const container = document.getElementById('calendar');
  //innerHTML で HTMLの中身を上書きする
  //divtタグはブロック要素で、横幅全体を占めるのが特徴、改行が入る。
  //spanタグは、インライン要素で、文章中の1部分に適用したいときなどに使う
  container.innerHTML = `
        <div class="calendar-nav">
            <button id="prev-month">\u2190</button>
            <span id="calendar-title"></span>
            <button id="next-month">\u2192</button>
        </div>
        <div id="calendar-table"></div>
    `;

  const titleEl = container.querySelector('#calendar-title');
  const tableWrap = container.querySelector('#calendar-table');
  let current = new Date();

  //カレンダーを表示する処理,例えば renderCalendar(2025, 5) なら、「2025年6月」のカレンダーを作ります
  //月が0スタートです
  function renderCalendar(year, month) {
    const today = new Date();
    const firstDay = (new Date(year, month, 1).getDay() + 6) % 7; //指定した年月の1日目の曜日を.getDay()で取得。戻り値は0～6で日曜スタートである。よって、月曜スタートとして、何曜日かを取得。
    const lastDate = new Date(year, month + 1, 0).getDate(); //次の月の0日目、つまり今月の最終日を取得する

    //表の見出し
    let html = '<table>';
    html +=
      '<tr><th>月</th><th>火</th><th>水</th><th>木</th><th>金</th><th>土</th><th>日</th></tr><tr>';

    //空白を作成（前の月のやつ）
    for (let i = 0; i < firstDay; i++) {
      html += '<td></td>';
    }

    //その月の1日～末までの日付を表に
    for (let day = 1; day <= lastDate; day++) {
      if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
        //day!==1はfirstDayが0の時（月曜）の時に改行しないための条件
        html += '</tr><tr>'; //改行
      }

      const isToday =
        day === today.getDate() &&
        month === today.getMonth() &&
        year === today.getFullYear(); //今日の年月、日を取得
      const cls = isToday ? ' class="today"' : ''; //今日の日付だったら、クラス属性付与、そうでないなら何も付与無し
      html += '<td' + cls + '>' + day + '</td>'; //日付
    }

    //残りの空日の月を作成（次の月分）
    const remaining = (firstDay + lastDate) % 7;
    if (remaining !== 0) {
      for (let i = 0; i < 7 - remaining; i++) {
        html += '<td></td>';
      }
    }
    html += '</tr></table>';
    tableWrap.innerHTML = html; //ここで、カレンダー表を一気に代入
    titleEl.textContent = `${year}年${month + 1}月`; //今月を代入、${}によって文字列の中に変数名を埋め込める
    document.dispatchEvent(new Event('calendarRendered')); //カレンダーの描画が終わった後に処理を追加用、今のところなくてもよい
  }

  //月を変更する関数
  function changeMonth(offset) {
    current.setMonth(current.getMonth() + offset);
    renderCalendar(current.getFullYear(), current.getMonth());
  }

  //ボタンクリック処理、←、→ボタンの処理、ボタンにidを振っており、e.target.idで取得している
  container.addEventListener('click', (e) => {
    if (e.target.id === 'prev-month') {
      changeMonth(-1); //前の月に変更
    } else if (e.target.id === 'next-month') {
      changeMonth(1); //次の月に変更
    }
  });

  //初期化、西暦と月を引数とする.ただし、getMonth()は(現在の月-1)であることに注意
  renderCalendar(current.getFullYear(), current.getMonth());
});
