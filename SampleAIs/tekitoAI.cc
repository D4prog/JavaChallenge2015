#include "bits/stdc++.h"

using namespace std;

#define FOR(i, a, b) for (int i = (a); i < (b); ++i)
#define REP(i, n) FOR(i, 0, n)

int board[40][40];
int life[4];
int turn;
int x[4];
int y[4];

int main() {
  cout << "Ready" << endl;
  while (true) {
    int my_num;
    //自分のプレイヤーIDを受け取る
    cin >> my_num;

    //ターン数を受け取る
    cin >> turn;

    //残機情報を受け取る
    REP(i, 4) { cin >> life[i]; }

    //盤面の情報を受け取る
    REP(i, 40) {
      REP(j, 40) { cin >> board[i][j]; }
    }

    //各AIの座標を受け取る
                REP(i, 4)
		{
			cin >> y[i] >> x[i];
		}

		//コマンドを出力
		cout << "U" << endl;
	}
}
