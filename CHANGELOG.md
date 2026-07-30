# 1.4.0

## Additions

- invalid upgrade entries are now displayed as "Invalid Upgrade"
  - upgrades are invalid if one of their components contains an invalid spell/attribute identifier
- added server config option to hide/show invalid upgrade_entries
- added a new built-in data pack that adds a number of enchantments. They grant the spell modifiers for the new weapon skills and are meant to be a replacement for the weapon skills tab. This data pack requires Spell Engine Extension.
- added built-in data packs for the RPG Series classes + their "Loot & Explore" add-on mods by Fichte. These data packs will automatically replace the default data packs when the corresponding add-on mods are loaded.
- added built-in data packs that add integration for various class mods.
  - "Druids (RPG Series Plus)" mod by Rulft44
  - "Bards (More RPG Series)" mod by Fichte
- added custom key binding category

## Changes

- split the built-in data packs, each content mod now has a dedicated compatibility pack which is automatically disabled if the mod is not installed
- fire mage class is no longer locked behind being in the nether by default
- updated all classes to their latest versions
- locked upgrades are no longer shown when changing upgrades is disabled, instead "Empty Upgrade" is displayed when no upgrade is selected 

## Fixes

- fixed a crash that sometimes occurred when displaying the class selection screen without having a class selected

## Technical

- refactored the class upgrade ids in the built-in data packs to be more in line with the new layout used in the Skill Tree mod

# 1.3.0

Updated to Spell Engine 1.9.5

## Additions

- added client config options for customizing the colours of the class name and description

## Changes

- updated the More RPG Series compat data pack and enabled it by default again

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