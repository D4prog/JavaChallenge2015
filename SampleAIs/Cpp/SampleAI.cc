#include <iostream>
#include <random>

#define REP(i, b, n) for (int i = (int)(b); i < (int)(n); ++i)
#define rep(i, n) REP(i, 0, n)

using namespace std;
typedef long long ll;

const string commands = "URDLA";
const int BOARD_WIDTH = 18;
const int FIELD_WIDTH = 6;
const int PLAYER_NUM = 4;

int turn, id;
int board[FIELD_WIDTH][FIELD_WIDTH];
int x[PLAYER_NUM];
int y[PLAYER_NUM];
string dir[PLAYER_NUM];
int pausing[PLAYER_NUM];

void action() {
  random_device rnd;
  int c = rnd() % commands.size();
  cout << commands[c] << endl;
}

int main() {
  cout << "READY" << endl;
  while (true) {
    cin >> id;
    cin >> turn;
    rep(i, FIELD_WIDTH) rep(j, FIELD_WIDTH) cin >> board[i][j];
    rep(i, PLAYER_NUM) cin >> y[i] >> x[i] >> dir[i] >> pausing[i];
    string eod;
    cin >> eod;

    action();
  }

  return 0;
}
