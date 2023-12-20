def label_hash(text: str) -> int:
    result = 0
    for ch in text.encode():
        result = (result + ch) * 17 % 256

    return result


def parse_step(step: str) -> (str, str, int):
    if step[-1] == '-':
        return step[:-1], label_hash(step[:-1]), 0
    return step[:-2], label_hash(step[:-2]), int(step[-1])


def part1():
    with open('input/day15.data') as f:
        texts = f.read().strip().split(',')

    result = 0
    for text in texts:
        result += label_hash(text)
    print(f'Part 1: {result}')


def part2():
    boxes: list[(str, int)] = [[] for _ in range(256)]
    with open('input/day15.data') as f:
        steps = f.read().strip().split(',')
    for step in steps:
        label, step_hash, focal = parse_step(step)
        if focal == 0:
            box = boxes[step_hash]
            for (i, lens) in enumerate(box):
                if lens[0] == label:
                    box.remove(lens)
                    break

        else:
            box = boxes[step_hash]
            for (i, lens) in enumerate(box):
                if lens[0] == label:
                    box[i] = (label, focal)
                    break
            else:
                box.append((label, focal))

    result = 0
    for n, box in enumerate(boxes, 1):
        for slot, lens in enumerate(box, 1):
            result += n * slot * lens[1]

    print(f'Part 2: {result}')


part1()
part2()

# Part 1: 515974
# Part 2: 265894
