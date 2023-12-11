import 'dart:io' show File;

void main() {
  var start = DateTime.now();
  print("Part 1: ${run(1)}");
  print("Part 2: ${run(999999)}");
  print("Total time: ${DateTime.now().difference(start)}");
}

int run(int increment) {
  var lines = File('input/day11.data').readAsLinesSync();
  var xs = List.filled(lines[0].length, 0);
  var ys = List.filled(lines.length, 0);

  var xInc = 0;
  for (var idx = 0; idx < lines[0].length; idx++) {
    xs[idx] = idx + xInc;
    if (lines.every((line) => line[idx] == '.')) {
      xInc += increment;
    }
  }

  var yInc = 0;
  for (var (idx, line) in lines.indexed) {
    ys[idx] = idx + yInc;
    if (line.split('').every((char) => char == '.')) {
      yInc += increment;
    }
  }

  var galaxies = <({int x, int y})>[];
  for (var y = 0; y < lines.length; y++) {
    var line = lines[y];
    for (var x = 0; x < line.length; x++) {
      if (line[x] == '#') {
        galaxies.add((x: xs[x], y: ys[y]));
      }
    }
  }

  var sum = 0;
  for (var i = 0; i < galaxies.length - 1; i++) {
    for (var j = i + 1; j < galaxies.length; j++) {
      sum += distance(galaxies[i], galaxies[j]);
    }
  }

  return sum;
}

int distance(({int x, int y}) a, ({int x, int y}) b) =>
    abs(a.x - b.x) + abs(a.y - b.y);
int abs(int x) => x * x.sign;

// Part 1: 9418609
// Part 2: 593821230983