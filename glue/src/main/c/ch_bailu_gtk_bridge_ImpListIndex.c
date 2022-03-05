/*
 * https://docs.gtk.org/gobject/tutorial.html
 */

#include "ch_bailu_gtk_bridge_ImpListIndex.h"
#include <glib-object.h>
#include <gio/gio.h>

static GType list_index_get_type (void);

typedef struct {
    GObjectClass parent_class;
} ListIndexClass;


typedef struct {
    GObject parent_instance;

    int index;
    int size;
} ListIndex;


enum
{
    PROP_0,
    PROP_ITEM_TYPE,
    N_PROPERTIES
};


static void list_index_iface_init (GListModelInterface *iface);
static void list_index_init (ListIndex *self);

static GType list_index_get_type_once (void);
static gpointer list_index_parent_class = ((void *)0);


// callback
static void list_index_get_property (GObject *object,
                                     guint property_id,
                                     GValue *value,
                                     GParamSpec *pspec)
{
    if (property_id == PROP_ITEM_TYPE) {
        g_value_set_gtype(value, list_index_get_type());
    } else {
        do {
            GObject *_glib__object = (GObject*) ((object));
            GParamSpec *_glib__pspec = (GParamSpec*) ((pspec)); guint _glib__property_id = ((property_id));
            g_log (((gchar*) 0), G_LOG_LEVEL_WARNING, "%s:%d: invalid %s id %u for \"%s\" of type '%s' in '%s'", "src/main/c/ch_bailu_gtk_bridge_ImpListIndex.c", 64, ("property"), _glib__property_id, _glib__pspec->name, g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__pspec))->g_class))->g_type)))), (g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__object))->g_class))->g_type))))));
        } while (0);
    }
}

// callback
static void list_index_set_property (GObject *object, guint property_id, const GValue *value, GParamSpec *pspec)
{
    if (property_id != PROP_ITEM_TYPE) {
        do {
            GObject *_glib__object = (GObject*) ((object)); GParamSpec *_glib__pspec = (GParamSpec*) ((pspec));
            guint _glib__property_id = ((property_id));
            g_log (((gchar*) 0), G_LOG_LEVEL_WARNING, "%s:%d: invalid %s id %u for \"%s\" of type '%s' in '%s'", "src/main/c/ch_bailu_gtk_bridge_ImpListIndex.c", 76, ("property"), _glib__property_id, _glib__pspec->name, g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__pspec))->g_class))->g_type)))), (g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__object))->g_class))->g_type))))));
        } while (0);
    }
}



// callback
static void list_index_dispose (GObject *object)
{
    ((((GObjectClass*) g_type_check_class_cast ((GTypeClass*) ((list_index_parent_class)), (((GType) ((20) << (2))))))))->dispose (object);
}


// callback
static gint ListIndex_private_offset;
static void list_index_class_intern_init (gpointer klass) {
    list_index_parent_class = g_type_class_peek_parent (klass);
    if (ListIndex_private_offset != 0) g_type_class_adjust_private_offset (klass, &ListIndex_private_offset);


    GObjectClass *object_class = ((((GObjectClass*) g_type_check_class_cast ((GTypeClass*) ((klass)), (((GType) ((20) << (2))))))));

    object_class->dispose = list_index_dispose;
    object_class->get_property = list_index_get_property;
    object_class->set_property = list_index_set_property;

    g_object_class_install_property (object_class, PROP_ITEM_TYPE,
                                     g_param_spec_gtype ("item-type", "", "", ((GType) ((20) << (2))),
                                                         G_PARAM_CONSTRUCT | G_PARAM_READWRITE | (G_PARAM_STATIC_NAME | G_PARAM_STATIC_NICK | G_PARAM_STATIC_BLURB)));

}


// 2. create this function
GType list_index_get_type (void) {
    static gsize static_g_define_type_id = 0;

    if (g_once_init_enter (&static_g_define_type_id)) {
        // only call needed
        GType g_define_type_id = list_index_get_type_once ();
        g_once_init_leave ((&static_g_define_type_id), (gsize) (g_define_type_id));

    }
    return static_g_define_type_id;
}



// this gets called once to get a type id and to initialize this class and interface
static GType list_index_get_type_once (void) {
    // first call
    GType g_define_type_id = g_type_register_static_simple (
            ((GType) ((20) << (2))),
            g_intern_static_string ("ListIndex"),
            sizeof (ListIndexClass),
            (GClassInitFunc)(void (*)(void)) list_index_class_intern_init, // <- callback
            sizeof (ListIndex),
            (GInstanceInitFunc)(void (*)(void)) list_index_init, // <- callback
            (GTypeFlags) 0);

    const GInterfaceInfo g_implement_interface_info = {
            (GInterfaceInitFunc)(void (*)(void)) list_index_iface_init, ((void *)0) , ((void *)0)
    };


    // second call
    g_type_add_interface_static (g_define_type_id, g_list_model_get_type (), &g_implement_interface_info);

    return g_define_type_id;
}

static GType list_index_get_item_type (GListModel *list)
{
    return list_index_get_type();
}


static guint list_index_get_n_items (GListModel *list)
{
    ListIndex *listIndex = (ListIndex*) g_type_check_instance_cast ((GTypeInstance*) (list), (list_index_get_type ()));
    return (guint) listIndex->size;
}


static gpointer list_index_get_item (GListModel *list,
                     guint index)
{
    ListIndex *result = g_object_new(list_index_get_type(), "item-type", list_index_get_type(), ((void *)0));
    ListIndex *listIndex = (ListIndex*) g_type_check_instance_cast ((GTypeInstance*) (list), (list_index_get_type ()));

    listIndex->index = index;
    result->index = index;
    result->size = listIndex->size;

    return result;
}


// callback
static void list_index_iface_init (GListModelInterface *iface)
{
    iface->get_item_type = list_index_get_item_type;
    iface->get_n_items = list_index_get_n_items;
    iface->get_item = list_index_get_item;
}


// callback
static void
list_index_init (ListIndex *listIndex)
{
    listIndex->index = 0;
    listIndex->size = 0;
}



__attribute__((visibility("default"))) jlong
Java_ch_bailu_gtk_bridge_ImpListIndex_create(JNIEnv *env, jclass klass)
{
    // 1. create g_object_new constructor
    return (jlong) g_object_new(list_index_get_type(), "item-type", list_index_get_type(), ((void *)0));
}

__attribute__((visibility("default"))) void
Java_ch_bailu_gtk_bridge_ImpListIndex_setSize (JNIEnv *env, jclass klass, jlong object, jint size)
{
    ListIndex *listIndex = (ListIndex*) object;

    guint old_size = (guint) listIndex->size;

    listIndex->size = (jint) size;

    g_list_model_items_changed (G_LIST_MODEL (listIndex), 0, old_size, size);
}

__attribute__((visibility("default"))) jint
Java_ch_bailu_gtk_bridge_ImpListIndex_getIndex (JNIEnv *env, jclass klass, jlong object)
{
    ListIndex *listIndex = (ListIndex*) object;
    return (jint) listIndex->index;
}


__attribute__((visibility("default"))) jint
Java_ch_bailu_gtk_bridge_ImpListIndex_getSize (JNIEnv *env, jclass klass, jlong object)
{
    ListIndex *listIndex = (ListIndex*) object;
    return (jint) listIndex->size;
}


__attribute__((visibility("default"))) void
Java_ch_bailu_gtk_bridge_ImpListIndex_setIndex (JNIEnv *env, jclass klass, jlong object, jint index)
{
    ListIndex *listIndex = (ListIndex*) object;
    listIndex->index = (int) index;
}
