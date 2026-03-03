# 1.2.0

## Changes

- changed classes/upgrades to no longer be unlocked by an advancement, but by a loot context predicate instead. (An example for this can be seen in the Fire Wizard class)
- improved the visual feedback on the class selection screen. 

## Technical

- reduced amount of data send to client when opening the class selection screen
- fixed log spam

# 1.1.0

Updated to Spell Engine 1.9 and RPG Inventory 2.10.0

## Additions

- class description field now supports longer descriptions

## Changes

- removed "class_item_identifier" field from rpg_class JSON files (now always uses the "spell_engine:spell_book" item)
- now uses the "class_item" equipment slot provided by RPG Inventory
- More RPG Series compat data pack is no longer enabled by default. This will be reverted, when all relevant mods have been updated.

# 1.0.0

First release.

#