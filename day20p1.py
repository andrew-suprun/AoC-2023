from typing import Protocol
from collections import deque


class Network(Protocol):
    def send(self, source: str, target: str, level: bool):
        ...


class Module(Protocol):
    def trigger(self, source: str, level: bool):
        ...


class ModuleNetwork:
    def __init__(self):
        self.modules = dict[str, Module]()
        self.signals = deque[tuple[str, str, bool]]()
        self.low_signals = 0
        self.high_signals = 0

    def send(self, source: str, target: str, level: bool):
        # print(f'{source} -{'high' if level else 'low'}-> {target}')
        if level:
            self.high_signals += 1
        else:
            self.low_signals += 1
        self.signals.append((source, target, level))

    def run(self):
        self.send("button", "broadcaster", False)
        while self.signals:
            signal = self.signals.popleft()
            module = self.modules.get(signal[1])
            if module:
                module.trigger(signal[0], signal[2])


class FlipFlop:
    targets: list[str]

    def __init__(self, name: str, network: Network):
        self.name = name
        self.network = network
        self.state = False

    def trigger(self, _source: str, level: bool):
        if not level:
            self.state = not self.state
            for out in self.targets:
                self.network.send(self.name, out, self.state)

    def __repr__(self):
        return f"FlipFlop: state={self.state} out={self.targets}"


class Conj:
    targets: list[str]

    def __init__(self, name: str, network: Network):
        self.name = name
        self.network = network
        self.state = dict[str, bool]()

    def trigger(self, source: str, level: bool):
        self.state[source] = level
        level = False if all(self.state.values()) else True
        for out in self.targets:
            self.network.send(self.name, out, level)

    def __repr__(self):
        return f"Conj: state={self.state} out={self.targets}"


class Broadcaster:
    targets: list[str]

    def __init__(self, name: str, network: Network):
        self.name = name
        self.network = network

    def trigger(self, _source: str, _level: bool):
        for target in self.targets:
            self.network.send("broadcaster", target, False)

    def __repr__(self):
        return f"Broadcaster: out={self.targets}"


def parse_input() -> ModuleNetwork:
    network = ModuleNetwork()
    with open("input/day20.data") as f:
        for line in f.readlines():
            full_name, output_str = line.strip().split(" -> ")
            module = network.modules.get(full_name[1:])
            if not module:
                if full_name[0] == "%":
                    module = FlipFlop(full_name[1:], network)
                    network.modules[full_name[1:]] = module
                elif full_name[0] == "&":
                    module = Conj(full_name[1:], network)
                    network.modules[full_name[1:]] = module
                else:
                    module = Broadcaster(full_name, network)
                    network.modules[full_name] = module
            module.targets = output_str.split(", ")

    for name, m in network.modules.items():
        for out in m.targets:
            receiver = network.modules.get(out)
            if type(receiver) is Conj:
                receiver.state[name] = False
    return network


def part1():
    network = parse_input()

    for _ in range(1000):
        network.run()

    print(f"Part 1: {network.low_signals * network.high_signals}")


part1()
# Part 1: 763500168
