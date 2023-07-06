# Changelog

All notable changes to this project will be documented in this file.

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