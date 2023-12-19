import 'dart:io';

void main() {
  var start = DateTime.now();
  var input = File('input/day10.data').readAsLinesSync();
  part1(input);
  part2(input);
  print(DateTime.now().difference(start));
}

void part1(List<String> input) {
  var maze = Maze(input);
  do {
    maze.step();
  } while (maze.char != 'S');
  print('Part 1: ${maze.steps ~/ 2}');
}

var graph = {
  '|': '│',
  '-': '─',
  'L': '╰',
  'J': '╯',
  '7': '╮',
  'F': '╭',
  '.': ' ',
  'S': 'S',
};

void part2(List<String> input) {
  var maze = Maze(input);
  var pipe = <Position>{};
  do {
    maze.step();
    pipe.add(maze.pos);
  } while (maze.char != 'S');

  var tiles = 0;
  for (var y = 0; y < maze.lines.length; y++) {
    var buf = StringBuffer();
    var inside = false;
    for (var x = 0; x < maze.lines[0].length; x++) {
      if (pipe.contains((y: y, x: x))) {
        var char = maze.lines[y][x];
        if (char == 'S') {
          char = maze.inferStartTile();
        }
        if (char == '|' || char == '7' || char == 'F') {
          inside = !inside;
        }
        buf.write(graph[char]);
      } else {
        if (inside) {
          tiles++;
          buf.write('▉');
        } else {
          buf.write(' ');
        }
      }
    }
    print(buf.toString());
  }
  print('Part 2: $tiles');
}

typedef Position = ({int y, int x});

enum Direction { north, east, south, west }

var start = 'S'.codeUnitAt(0);

class Maze {
  List<String> lines;
  Position pos = (y: 0, x: 0);
  Direction dir = Direction.west;
  int steps = 0;
  String get char => lines[pos.y][pos.x];

  Maze(this.lines) {
    loop:
    for (var (y, line) in lines.indexed) {
      for (var (x, char) in line.codeUnits.indexed) {
        if (char == start) {
          pos = (x: x, y: y);
          break loop;
        }
      }
    }

    if ('-LF'.contains(lines[pos.y][pos.x - 1])) {
      dir = Direction.west;
    } else if ('-J7'.contains(lines[pos.y][pos.x + 1])) {
      dir = Direction.east;
    } else if ('|7F'.contains(lines[pos.y - 1][pos.x])) {
      dir = Direction.north;
    } else if ('|JL'.contains(lines[pos.y + 1][pos.x])) {
      dir = Direction.south;
    } else {
      throw Error();
    }
  }

  String inferStartTile() {
    var connectWest = '-LF'.contains(lines[pos.y][pos.x - 1]);
    var connectEast = '-J7'.contains(lines[pos.y][pos.x + 1]);
    var connectNorth = '|7F'.contains(lines[pos.y - 1][pos.x]);
    var connectSouth = '|JL'.contains(lines[pos.y + 1][pos.x]);
    if (connectSouth) {
      if (connectNorth) return '|';
      if (connectWest) return '7';
      if (connectEast) return 'F';
    }
    if (connectNorth) {
      if (connectWest) return 'J';
      if (connectEast) return 'L';
    }
    if (connectEast && connectWest) return '-';
    return ' ';
  }

  void step() {
    switch (dir) {
      case Direction.north:
        pos = (y: pos.y - 1, x: pos.x);
        dir = switch (lines[pos.y][pos.x]) {
          '7' => Direction.west,
          'F' => Direction.east,
          _ => dir,
        };
      case Direction.east:
        pos = (y: pos.y, x: pos.x + 1);
        dir = switch (lines[pos.y][pos.x]) {
          'J' => Direction.north,
          '7' => Direction.south,
          _ => dir,
        };
      case Direction.south:
        pos = (y: pos.y + 1, x: pos.x);
        dir = switch (lines[pos.y][pos.x]) {
          'J' => Direction.west,
          'L' => Direction.east,
          _ => dir,
        };
      case Direction.west:
        pos = (y: pos.y, x: pos.x - 1);
        dir = switch (lines[pos.y][pos.x]) {
          'L' => Direction.north,
          'F' => Direction.south,
          _ => dir,
        };
    }
    steps++;
  }

  @override
  String toString() =>
      'char: $char, pos: $pos, dir: ${dir.toString()}, steps: $steps';
}

// 7102
// 363