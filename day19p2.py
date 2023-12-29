class Constraint:
    def __init__(self, xmas: str, op: str, rate: int):
        self.xmas = xmas
        self.op = op
        self.rate = rate

    def __repr__(self):
        return f'{self.xmas} {self.op} {self.rate}'


class Rule:
    def __init__(self, constraints: list[Constraint], next_wf: str):
        self.constraints = constraints
        self.next_wf = next_wf

    def __repr__(self):
        return f'{self.constraints} -> {self.next_wf}'


def parse_rule(rule: str) -> Rule:
    rate, next_wf = rule[2:].split(':')
    return Rule([Constraint(rule[0], rule[1], int(rate))], next_wf)


class Workflow:
    def __init__(self, line):
        self.name, rules_str = line.strip('}').split('{')
        rules = [rule for rule in rules_str.split(',')]
        self.rules = [parse_rule(op) for op in rules[:-1]]
        last_constraints = list[Constraint]()
        for rule in self.rules:
            for constraint in rule.constraints:
                if constraint.op == '<':
                    last_constraints.append(Constraint(constraint.xmas, '>', constraint.rate - 1))
                else:
                    last_constraints.append(Constraint(constraint.xmas, '<', constraint.rate + 1))
        self.rules.append(Rule(last_constraints, rules[-1]))

    def __repr__(self):
        return f'{self.name}: ops={self.rules}'


with open('input/day19.data') as f:
    text = f.read()
rules_text, ratings_text = text.split('\n\n')
rules = [Workflow(line) for line in rules_text.splitlines()]
workflows = dict((wf.name, wf) for wf in rules)


def calc_accepted(constraints: list[Constraint]) -> int:
    xmas = [range(1, 4001)] * 4
    for c in constraints:
        idx = 'xmas'.index(c.xmas)
        r = xmas[idx]
        if c.op == '<':
            r = range(r.start, min(r.stop, c.rate))
        else:
            r = range(max(r.start, c.rate+1), r.stop)
        xmas[idx] = r

    result = 1
    for r in xmas:
        if r.start < r.stop: # ???
            result *= r.stop - r.start
    return result


def part2(name: str, constraints: list[Constraint]) -> int:
    if name == 'R':
        return 0
    if name == 'A':
        return calc_accepted(constraints)
    result = 0
    wf = workflows[name]
    for (i, rule) in enumerate(wf.rules):
        new_constraints = constraints[:]
        for prev_rule in wf.rules[:i]:
            for c in prev_rule.constraints:
                if c.op == '<':
                    new_constraints.append(Constraint(c.xmas, '>', c.rate-1))
                else:
                    new_constraints.append(Constraint(c.xmas, '<', c.rate+1))
        new_constraints.extend(rule.constraints)
        result += part2(rule.next_wf, new_constraints)

    return result


print(f'Part 2: {part2('in', [])}')
# Part 2: 131619440296497