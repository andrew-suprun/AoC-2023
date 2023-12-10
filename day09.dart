import 'dart:io';

void main() async {
  final (part1, part2) = (await File("input.data").readAsLines())
      .map((line) => solve(line))
      .reduce((acc, sol) => (acc.$1 + sol.$1, acc.$2 + sol.$2));
  print("Part1: $part1\nPart2: $part2");
}

(int, int) solve(String line) {
  var history = line.split(" ").map((d) => int.parse(d)).toList();
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