import 'dart:io' show File;

class Record {
  String scheme;
  List<int> check;

  Record(this.scheme, this.check);

  @override
  bool operator ==(Object other) {
    if (other is! Record) return false;
    if (scheme != other.scheme) return false;
    if (check.length != other.check.length) return false;
    for (var (idx, item) in check.indexed) {
      if (item != other.check[idx]) return false;
    }
    return true;
  }

  @override
  int get hashCode {
    var result = scheme.hashCode;

    for (var item in check) {
      result = result * 41 + item.hashCode;
    }
    return result;
  }
}

void main() {
  var start = DateTime.now();
  print('Part 1: ${run(part1)}');
  print('Part 2: ${run(part2)}');
  print('Total time: ${DateTime.now().difference(start)}');
}

int run(String Function(String) convert) {
  return File('input/day12.data')
      .readAsLinesSync()
      .map((line) => solve(parseLine(convert(line))))
      .reduce((acc, value) => acc + value);
}

String part1(String line) => line;

String part2(String line) {
  var [scheme, check] = line.split(' ');
  var scheme5 = List.filled(5, scheme);
  var check5 = List.filled(5, check);
  return '${scheme5.join('?')} ${check5.join(',')}';
}

Record parseLine(String line) {
  var [scheme, checkPart] = line.split(' ');
  var check = checkPart.split(',').map((e) => int.parse(e)).toList();
  return Record('$scheme.', check);
}

var cache = <Record, int>{};

int solve(Record record) {
  if (record.scheme == '') return record.check.isEmpty ? 1 : 0;

  if (record.check.isEmpty) return record.scheme.contains('#') ? 0 : 1;

  var cached = cache[record];
  if (cached != null) return cached;

  var result = 0;

  if (".?".contains(record.scheme[0])) {
    result += solve(Record(record.scheme.substring(1), record.check));
  }

  if ('#?'.contains(record.scheme[0])) {
    if (record.check[0] <= record.scheme.length &&
        !record.scheme.substring(0, record.check[0]).contains('.') &&
        (record.check[0] == record.scheme.length ||
            record.scheme[record.check[0]] != "#")) {
      result += solve(Record(record.scheme.substring(record.check[0] + 1),
          record.check.sublist(1)));
    }
  }

  cache[record] = result;
  return result;
}

// Part 1: 7163
// Part 2: 17788038834112