import 'dart:io';

typedef Location = ({int y, int x});

Future<void> main() async {
  var start = DateTime.now();
  print("Part 1: ${await run(1)}");
  print("Part 2: ${await run(999999)}");
  print("Total time: ${DateTime.now().difference(start)}");
}

Future<int> run(int increment) async {
  var lines = await File('input.data').readAsLines();
  var xs = List<int>.filled(lines[0].length, 0);
  var ys = List<int>.filled(lines.length, 0);

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

  var galaxies = List<Location>.empty(growable: true);
  for (var y = 0; y < lines.length; y++) {
    var line = lines[y];
    for (var x = 0; x < line.length; x++) {
      if (line[x] == '#') {
        galaxies.add((y: ys[y], x: xs[x]));
      }
    }
  }

  var sum = 0;
  for (var i = 0; i < galaxies.length - 1; i++) {
    for (var j = i + 1; j < galaxies.length; j++) {
      sum += abs(galaxies[i].x - galaxies[j].x) +
          abs(galaxies[i].y - galaxies[j].y);
    }
  }

  return sum;
}

int abs(int x) => x < 0 ? -x : x;


// 9418609
// 593821230983