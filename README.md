# Enhanced Periphery

Adds some quality-of-life features to CC: Tweaked peripherals.

New functionality:
- `listWithCapabilities()`: Same as `list()`, but adds `hasEnergy`, `hasFluid`, `hasInventory` if the item supports those capabilities.
- `getItemInventory(slot)`: Retrieves the inventory of a specified slot's item, in `listWithCapabilities` format.
- `getItemInventoryDeep(slot)`: Same as `getItemInventory`, but recursively lists nested inventories.

Enhanced functionality:
- `getItemDetail()` returns `energy` and `tanks` keys for stored FE and fluids, as applicable.
