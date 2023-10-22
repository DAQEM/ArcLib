# Changelog

All notable changes to this project will be documented in this file.

## [1.3]

### Fixed 
- onEffectAdded Action not being called the first time.

## [0.9.1]
### Added
- Damage Source Condition.

[0.9.1]: https://github.com/DAQEM/Arc/compare/60021b402788f40abb454270ef7bab1afd2b3da4...25ac8ca910647bf986929c2dbd289739ff4ab4d4


## [0.9]
### Added
- Damage Multiplier Reward.
- pickup delay to Drop Item Reward.

[0.9]: https://github.com/DAQEM/Arc/compare/d86fc894198d0826b7dbdecbe0a5b87a2799a938...60021b402788f40abb454270ef7bab1afd2b3da4

## [0.8.7]
### Added
- Not Condition.

[0.8.7]: https://github.com/DAQEM/Arc/compare/bc45351dc5e7f3542ae0bb76bc705b8cb61adcf8...d86fc894198d0826b7dbdecbe0a5b87a2799a938

## [0.8.6]
### Fixed
- Registry default type checking.
- Block drop multiplier item drop amount calculation.

[0.8.6]: https://github.com/DAQEM/Arc/compare/790392c3b45b5a2df454a6ba8a6772371735ceac...bc45351dc5e7f3542ae0bb76bc705b8cb61adcf8

## 0.8.5
### Fixed
- Turned off debug mode.

## [0.8.4]
### Added
- Static Action Holder condition.

[0.8.4]: https://github.com/DAQEM/Arc/compare/7d1fbddb2137e80350a0a63d129078659e54883b...1ebaa35771e85db2973d4791e2ce8f0ea92cd6b0

## [0.8.3]
### Added
- Static Action Holder condition.

[0.8.3]: https://github.com/DAQEM/Arc/compare/1d19d8b9c1e8cf09f8f5bc0254df15773e1f5a30...7d1fbddb2137e80350a0a63d129078659e54883b

## [0.8.2]
### Fixed
- Set GetDestroySpeedAction to perform on client.

[0.8.2]: https://github.com/DAQEM/Arc/compare/166fd568eeceb9cde52e73e9fe09876c2d670df5...1d19d8b9c1e8cf09f8f5bc0254df15773e1f5a30

## [0.8]
### Fixed
- Crash when there was no event subscriber for the before action event.

[0.8]: https://github.com/DAQEM/Arc/compare/c78aec18fb66fec8b039caecdf683c2171b48363...3bd17d3ff7d8451b0c92b8d513cf9254b718514a

## [0.7]
### Added
- Before action event.

### Changed
- Made place and break block actions cancelable.
- Made hurt entity action cancelable.

[0.6]: https://github.com/DAQEM/Arc/compare/c8ef659b10c8e66f19e09a198e86154ce111a60a...c78aec18fb66fec8b039caecdf683c2171b48363


## [0.6.3]
### Fixed
- EntityTypes serializer.

[0.6.3]: https://github.com/DAQEM/Arc/compare/02c414f7dae0e8c606fddda0eaddf33be63547b5...c8ef659b10c8e66f19e09a198e86154ce111a60a

## [0.6.2]
### Fixed
- OrCondition serializer.

[0.6.2]: https://github.com/DAQEM/Arc/compare/a5d62eb40733fc4bda4f2ba7454684d5b7961ca8...02c414f7dae0e8c606fddda0eaddf33be63547b5

## [0.6.1]
### Changed
- Made registries final.

[0.6.1]: https://github.com/DAQEM/Arc/compare/fea0bcf479e223dc9daa72965ed290ecc66b1b96...a5d62eb40733fc4bda4f2ba7454684d5b7961ca8

## [0.6]
### Removed
- Reload listener events.

[0.6]: https://github.com/DAQEM/Arc/compare/47cf6bff91bea433511be6a2720b48a7961a3f3d...fea0bcf479e223dc9daa72965ed290ecc66b1b96

## [0.5]
### Added
- Action events.
- Action holder manager.

[0.5]: https://github.com/DAQEM/Arc/compare/e7dd6645b9dc0481c82750b6c60eac5afc0bc1d6...47cf6bff91bea433511be6a2720b48a7961a3f3d

## [0.4]
### Added
- Reload listener events.
### Fixed
- Player Mixin structure.

[0.4]: https://github.com/DAQEM/Arc/compare/7b22b74cf699627406d061901aad7455375d4c43...e7dd6645b9dc0481c82750b6c60eac5afc0bc1d6

## [0.3.1] - 2023-07-05
### Changed
- Moved init registry to run after mods init.

[0.3.1]: https://github.com/DAQEM/Arc/compare/e5cdcb7361ea44a143ab40251f2f5b0aec9b9114...7b22b74cf699627406d061901aad7455375d4c43

## [0.3] - 2023-07-05
### Added
- Min > max check where needed.
- Custom registry events.

### Removed
- `getActionHolders` from `IActionHolderType`.

[0.3]: https://github.com/DAQEM/Arc/compare/2b0b77380f0a6867fa71cdcce0c6743444a39892...e5cdcb7361ea44a143ab40251f2f5b0aec9b9114

## [0.2] - 2023-07-05
### Fixed
- Made `ActionDataType` register function publicly accessible.
### Added
- `getType` to `IActionHolder`.
- `getSourceActionHolder` to `ActionData`.

[0.2]: https://github.com/DAQEM/Arc/compare/26bc895c88c64b6a746a41e0b0ebe9f387108098...2b0b77380f0a6867fa71cdcce0c6743444a39892
