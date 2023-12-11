import 'dart:io' show File;

void main() {
  var start = DateTime.now();
  var (part1, part2) = File('input/day09.data').readAsLinesSync()
      .map((line) => solve(line))
      .reduce((acc, sol) => (acc.$1 + sol.$1, acc.$2 + sol.$2));
  print('Part 1: $part1\nPart 2: $part2');
  print(DateTime.now().difference(start));
}

(int, int) solve(String line) {
  var history = line.split(' ').map((d) => int.parse(d)).toList();
  var (part1, part2, sign) = (0, 0, 1);
  while (history.any((number) => number != 0)) {
    part1 += history.last;
    part2 += history.first * sign;
    sign = -sign;
    for (var i = 0; i < history.length - 1; i++) {
      history[i] = history[i + 1] - history[i];
    }
    history.removeLast();
  }

  return (part1, part2);
}

// Part 1: 1953784198
// Part 2: 957