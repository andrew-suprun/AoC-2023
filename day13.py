def transpose(group: list[str]) -> list[str]:
    return [''.join(t) for t in list(zip(*group))]


def diff(a, b):
    return sum(ca != cb for (ca, cb) in zip(a, b))


def find_mirror(group: list[str], errors: int) -> int:
    for i in range(0, len(group) - 1):
        total = 0
        for (j, k) in zip(range(i, -1, -1), range(i + 1, len(group))):
            for (cj, ck) in zip(group[j], group[k]):
                total += cj != ck
        if total == errors:
            return i + 1
    return 0


def solve(errors: int) -> int:
    with open('input/day13.data') as f:
        text = f.read()
    groups = text.split('\n\n')
    result = 0
    for g in groups:
        group = [line.strip() for line in g.splitlines()]
        tr = transpose(group)
        result += 100 * find_mirror(group, errors) + find_mirror(tr, errors)
    return result


print(f'Part 1: {solve(0)}')
print(f'Part 2: {solve(1)}')

# Part 1: 30487
# Part 2: 31954