#include <stdio.h>
#include <malloc.h>

typedef struct {
    int x;
    int y;
} Point;


typedef enum {
    WTYPE,
    ATYPE,
    MTYPE
} UnitType;

typedef struct {
    char *name;
    Point position;
    int health;
    UnitType type;
} Unit;


typedef struct {
    Unit *unit;
    int size;
} Array;

void Print(Array *arr) {
    if (arr == NULL) {
        printf("Array is NULL\n");
    }
    for (int i = 0; i < arr->size; ++i) {
        Unit unit = arr->unit[i];
        char *typeStr;
        switch (unit.type) {
            case WTYPE: typeStr = "W"; break;
            case ATYPE: typeStr = "A"; break;
            case MTYPE: typeStr = "M"; break;
            default: typeStr = "Unknown";
        }
        printf("[%d] %s: Type=%s, Health=%d, Position=(%d,%d)\n",
               i, unit.name, typeStr, unit.health,
               unit.position.x, unit.position.y);
    }


}

void AddNewUnit(Array *arr, Unit *newUnit) {
    void * temp = realloc(arr->unit, (arr->size + 1) * sizeof(Unit));
    if (temp == NULL) {
        return;
    }
    arr->unit = temp;
    arr->unit[arr->size] = *newUnit;
    arr->size++;
}

void RemoveUnit(Array *arr,int index) {
    for (int i = index; i < arr->size - 1; i++) {
        arr->unit[i] = arr->unit[i + 1];
    }
    arr->size--;
    void * temp = realloc(arr->unit, arr->size * sizeof(Unit));
    if (temp != NULL) {
        arr->unit = temp;
    }

}


int main() {
    Array arr = {NULL, 0};
    Unit u1 = {"A", {0, 0}, 100, WTYPE};
    Unit u2 = {"B", {1, 1}, 90, ATYPE};
    AddNewUnit(&arr, &u1);
    AddNewUnit(&arr, &u2);
    Print(&arr);
    printf("Size: %d\n", arr.size);
    RemoveUnit(&arr, 0);
    Print(&arr);
    printf("Size: %d\n", arr.size);

    free(arr.unit);
    return 0;


}
