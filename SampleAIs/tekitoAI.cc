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
    cin >> my_num;

    cin >> turn;

    REP(i, 4) { cin >> life[i]; }

    REP(i, 40) {
      REP(j, 40) { cin >> board[i][j]; }
    }

    REP(i, 4) { cin >> y[i] >> x[i]; }
    cout << "U" << endl;
  }
}