from collections import deque


class Network:
    def __init__(self, modules: dict[str, any]):
        self.modules = modules
        self.signals = deque[(str, str, bool)]()
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
        self.send('button', 'broadcaster', False)
        while self.signals:
            signal = self.signals.popleft()
            m = self.modules.get(signal[1])
            if m:
                m.trigger(signal[0], signal[2], self)


class FlipFlop:
    def __init__(self, name: str):
        self.name = name
        self.state = False
        self.targets = list[str]()

    def trigger(self, source: str, level: bool, network: Network):
        if not level:
            self.state = not self.state
            for out in self.targets:
                network.send(self.name, out, self.state)

    def __repr__(self):
        return f'FlipFlop: state={self.state} out={self.targets}'


class Conj:
    def __init__(self, name: str):
        self.name = name
        self.state = dict[str, bool]()
        self.targets = list[str]()

    def trigger(self, source: str, level: bool, network: Network):
        self.state[source] = level
        level = False if all(self.state.values()) else True
        for out in self.targets:
            network.send(self.name, out, level)

    def __repr__(self):
        return f'Conj: state={self.state} out={self.targets}'


class Broadcaster:
    def __init__(self, name: str):
        self.name = name
        self.targets = list[str]()

    def trigger(self, _source: str, _target: str, network: Network):
        for target in self.targets:
            network.send('broadcaster', target, False)

    def __repr__(self):
        return f'Broadcaster: out={self.targets}'


def parse_input() -> dict[str, any]:
    modules = dict[str, any]()
    with open('input/day20.data') as f:
        for line in f.readlines():
            full_name, output_str = line.strip().split(' -> ')
            targets: list[str] = output_str.split(', ')
            module = modules.get(full_name[1:])
            if not module:
                if full_name[0] == '%':
                    module = FlipFlop(full_name[1:])
                    modules[full_name[1:]] = module
                elif full_name[0] == '&':
                    module = Conj(full_name[1:])
                    modules[full_name[1:]] = module
                else:
                    module = Broadcaster(full_name)
                    modules[full_name] = module
            module.targets = targets

    for (name, m) in modules.items():
        for out in m.targets:
            receiver = modules.get(out)
            if type(receiver) is Conj:
                receiver.state[name] = False
    return modules


network = Network(parse_input())

for _ in range(1000):
    network.run()

print(f'Part 1: {network.low_signals * network.high_signals}')
# Part 1: 763500168
