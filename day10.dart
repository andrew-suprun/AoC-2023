import 'dart:io';

void main() async {
  part1();
  part2();
}

part1() async {
  final maze = Maze(await File("input.data").readAsLines());
  do {
    maze.step();
  } while (maze.char != 'S');
  print(maze);
  print(maze.steps ~/ 2);
}

final graph = {
  "|": "│",
  "-": "─",
  "L": "└",
  "J": "┘",
  "7": "┐",
  "F": "┌",
  ".": " ",
  "S": "S",
};

part2() async {
  final maze = Maze(await File("input.data").readAsLines());
  var pipe = Set<Position>();
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
  print(tiles);
}

typedef Position = ({int y, int x});

enum Direction { North, East, South, West }

final start = 'S'.codeUnitAt(0);

class Maze {
  final List<String> lines;
  Position pos = (y: 0, x: 0);
  Direction dir = Direction.West;
  int steps = 0;
  String get char => lines[pos.y][pos.x];

  Maze(this.lines) {
    loop:
    for (final (y, line) in lines.indexed) {
      for (final (x, char) in line.codeUnits.indexed) {
        if (char == start) {
          this.pos = (y: y, x: x);
          break loop;
        }
      }
    }

    if ("-LF".contains(lines[pos.y][pos.x - 1])) {
      dir = Direction.West;
    } else if ("-J7".contains(lines[pos.y][pos.x + 1])) {
      dir = Direction.East;
    } else if ("|7F".contains(lines[pos.y - 1][pos.x])) {
      dir = Direction.North;
    } else if ("|JL".contains(lines[pos.y + 1][pos.x])) {
      dir = Direction.South;
    } else {
      throw Error();
    }
  }

  String inferStartTile() {
    final connectWest = "-LF".contains(lines[pos.y][pos.x - 1]);
    final connectEast = "-J7".contains(lines[pos.y][pos.x + 1]);
    final connectNorth = "|7F".contains(lines[pos.y - 1][pos.x]);
    final connectSouth = "|JL".contains(lines[pos.y + 1][pos.x]);
    if (connectSouth) {
      if (connectNorth) return "|";
      if (connectWest) return '7';
      if (connectEast) return 'F';
    }
    if (connectNorth) {
      if (connectWest) return 'J';
      if (connectEast) return 'L';
    }
    if (connectEast && connectWest) return "-";
    return ' ';
  }

  step() {
    switch (dir) {
      case Direction.North:
        pos = (y: pos.y - 1, x: pos.x);
        switch (lines[pos.y][pos.x]) {
          case '7':
            dir = Direction.West;
          case 'F':
            dir = Direction.East;
        }
      case Direction.East:
        pos = (y: pos.y, x: pos.x + 1);
        switch (lines[pos.y][pos.x]) {
          case 'J':
            dir = Direction.North;
          case '7':
            dir = Direction.South;
        }
      case Direction.South:
        pos = (y: pos.y + 1, x: pos.x);
        switch (lines[pos.y][pos.x]) {
          case 'J':
            dir = Direction.West;
          case 'L':
            dir = Direction.East;
        }
      case Direction.West:
        pos = (y: pos.y, x: pos.x - 1);
        switch (lines[pos.y][pos.x]) {
          case 'L':
            dir = Direction.North;
          case 'F':
            dir = Direction.South;
        }
    }
    steps++;
  }

  String toString() =>
      "char: $char, pos: $pos, dir: ${dir.toString()}, steps: $steps";
}

// 7102
// 363