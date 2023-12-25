def parse_rating(rating: list[str]) -> (str, int):
    return rating[0], int(rating[1])


def parse_ratings(rating: str) -> dict[str, int]:
    return dict(parse_rating(attr.split('=')) for attr in rating.strip('{}').split(','))


def parse_op(op: str) -> (str, str, str, str):
    rate, next_wf = op[2:].split(':')
    return op[0], op[1], int(rate), next_wf


class Workflow:

    def __init__(self, line):
        self.name, rules_str = line.strip('}').split('{')
        rules = [rule for rule in rules_str.split(',')]
        self.ops = [parse_op(op) for op in rules[:-1]]
        self.last = rules[-1]

    def apply(self, ratings: dict[str, int]) -> str:
        for op in self.ops:
            rating = ratings[op[0]]
            if op[1] == '<' and rating < op[2]:
                return op[3]
            elif op[1] == '>' and rating > op[2]:
                return op[3]
        return self.last

    def __repr__(self):
        return f'ops={self.ops}, last={self.last}'


def solve() -> int:
    with open('input/day19.data') as f:
        text = f.read()
    rules_text, ratings_text = text.split('\n\n')
    rules = [Workflow(line) for line in rules_text.splitlines()]
    workflows = dict((wf.name, wf) for wf in rules)
    ratings = [parse_ratings(line) for line in ratings_text.splitlines()]
    total = 0
    for rating in ratings:
        name = 'in'
        while True:
            next = workflows[name].apply(rating)
            if next == 'A':
                total += sum(rating.values())
                break
            elif next == 'R':
                break
            else:
                name = next
    return total


print(f'Part 1: {solve()}')

# Part 1: 287054
