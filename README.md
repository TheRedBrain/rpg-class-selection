# RPG Class Selection

This mod adds a class selection screen known from many RPG games.

Classes consist of "upgrades", which can be spells (powered by Spell Engine), entity attribute modifiers or a
combination of both.

## Built-in classes

The mod comes with built-in data/resource packs that add classes based on the RPG Series mods by Daedelus and the More
RPG Series by Fichte.

Feel free to learn from those packs or use them as a baseline for a more customized experience.

## How can the screen be accessed?

There are 4 ways to open the class selection screen:

- a hot key (defaults to "K"). This can be disabled in the server config.
- the screen opens for players when they first join a world. This can be disabled in the server config.
- interacting with the "RPG Class Selection Block". This block is not available in survival and is intended for
  mapmakers and server admins.
- other mods can implement custom methods to open the screen, using the provided API method.

Each method can customize the screen that is opened using config options, block entity data or method arguments.

Customization options include:

- what class is shown when the screen is opened
- whether the class list is restricted to the initial class and the current class of the player
- whether the player can change the class
- whether the player can change the upgrades of the current class

## Additional customization

The mod comes with different layouts for the class selection screen. What layout is used can be changed in the server
config.

## How does it work?

When a player chooses a class, an item defined by the class is placed into a specific equipment slot.
Selected upgrades are stored on that item.

> Currently, the equipment slot used is provided by RPG Inventory.\
> Alternative compatibility with Trinkets is getting explored and might be added at a later date.

Classes and class upgrades can be unlocked with advancements.

Classes are defined via data packs.

Example class:

```json
{
  "class_identifier": "rpgclassselection:fire_wizard",
  "unlock_advancement_identifier": "minecraft:story/smelt_iron",
  "class_item_identifier": "wizards:fire_spell_book",
  "visible_when_locked": true,
  "description": "class_selection_screen.rpgclassselection.fire_wizard.description",
  "locked_description": "class_selection_screen.rpgclassselection.fire_wizard.locked_description",
  "upgrade_entry_group_list": [
    {
      "upgrade_entry_list": [
        {
          "upgrade_identifier": "rpgclassselection:fire_spell_1",
          "unlock_advancement_identifier": "minecraft:story/mine_diamond",
          "visible_when_locked": true,
          "title": "spell.wizards.fire_blast.name",
          "icon_path": "wizards:textures/spell/fire_blast.png",
          "component_list": [
            {
              "type": "spell",
              "spell_identifier": "wizards:fire_blast",
              "attribute_identifier": "",
              "attribute_modifier_amount": 0.0,
              "attribute_modifier_operation": ""
            },
            {
              "type": "attribute_modifier",
              "spell_identifier": "",
              "attribute_identifier": "minecraft:generic.attack_damage",
              "attribute_modifier_amount": 3.0,
              "attribute_modifier_operation": "ADD_VALUE"
            }
          ]
        }
      ]
    },
    {},
    {
      "upgrade_entry_list": [
        {
          "upgrade_identifier": "rpgclassselection:fire_spell_2",
          "title": "spell.wizards.fire_breath.name",
          "icon_path": "wizards:textures/spell/fire_breath.png",
          "component_list": [
            {
              "type": "spell",
              "spell_identifier": "wizards:fire_breath"
            }
          ]
        }
      ]
    }
  ]
}
```
