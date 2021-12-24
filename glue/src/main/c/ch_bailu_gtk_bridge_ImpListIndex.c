/*
 * https://docs.gtk.org/gobject/tutorial.html
 */

#include "ch_bailu_gtk_bridge_ImpListIndex.h"
#include <glib-object.h>
#include <gio/gio.h>

G_BEGIN_DECLS

#define LIST_TYPE_INDEX list_index_get_type()

G_DECLARE_FINAL_TYPE(ListIndex, list_index, LIST, INDEX, GObject)

ListIndex *list_index_new(void);

G_END_DECLS

struct _ListIndex
{
    GObject parent_instance;

    int index;
    int size;
};


enum
{
    PROP_0,
    PROP_ITEM_TYPE,
    N_PROPERTIES
};


static void list_index_iface_init (GListModelInterface *iface);

G_DEFINE_TYPE_WITH_CODE (ListIndex, list_index, G_TYPE_OBJECT,
                         G_IMPLEMENT_INTERFACE (G_TYPE_LIST_MODEL, list_index_iface_init));


static void
list_index_items_changed   (ListIndex *listIndex,
                            guint       index,
                            guint       removed,
                            guint       added)
{
    g_list_model_items_changed (G_LIST_MODEL (listIndex), index, removed, added);
}


static void
list_index_dispose (GObject *object)
{
    G_OBJECT_CLASS (list_index_parent_class)->dispose (object);
}


static void
list_index_get_property   (GObject    *object,
                           guint       property_id,
                           GValue     *value,
                           GParamSpec *pspec)
{
    if (property_id == PROP_ITEM_TYPE) {
        g_value_set_gtype(value, LIST_TYPE_INDEX);
    } else {
        G_OBJECT_WARN_INVALID_PROPERTY_ID(object, property_id, pspec);
    }
}


static void
list_index_set_property   (GObject      *object,
                           guint         property_id,
                           const GValue *value,
                           GParamSpec   *pspec)
{
    if (property_id != PROP_ITEM_TYPE) {
        G_OBJECT_WARN_INVALID_PROPERTY_ID(object, property_id, pspec);
    }
}


static void
list_index_class_init (ListIndexClass *klass)
{
    GObjectClass *object_class = G_OBJECT_CLASS (klass);

    object_class->dispose      = list_index_dispose;
    object_class->get_property = list_index_get_property;
    object_class->set_property = list_index_set_property;

    g_object_class_install_property (object_class, PROP_ITEM_TYPE,
                                     g_param_spec_gtype ("item-type", "", "", G_TYPE_OBJECT,
                                                         G_PARAM_CONSTRUCT | G_PARAM_READWRITE | G_PARAM_STATIC_STRINGS));
}


static GType
list_index_get_item_type (GListModel *list)
{
    return LIST_TYPE_INDEX;
}


static guint
list_index_get_n_items (GListModel *list)
{
    ListIndex *listIndex = LIST_INDEX (list);
    return (guint) listIndex->size;
}


static gpointer
list_index_get_item (GListModel *list,
                     guint index)
{
    ListIndex *result = list_index_new();
    ListIndex *listIndex = LIST_INDEX (list);

    listIndex->index  = index;
    result->index     = index;
    result->size         = listIndex->size;

    return result;
}


static void
list_index_iface_init (GListModelInterface *iface)
{
    iface->get_item_type = list_index_get_item_type;
    iface->get_n_items   = list_index_get_n_items;
    iface->get_item      = list_index_get_item;
}


static void
list_index_init (ListIndex *listIndex)
{
    listIndex->index = 0;
    listIndex->size = 0;
}

ListIndex *
list_index_new(void)
{
    return g_object_new(LIST_TYPE_INDEX, "item-type", LIST_TYPE_INDEX, NULL);
}

JNIEXPORT jlong JNICALL
Java_ch_bailu_gtk_bridge_ImpListIndex_create(JNIEnv *env, jclass klass)
{
    return (jlong) list_index_new();
}

JNIEXPORT void JNICALL
Java_ch_bailu_gtk_bridge_ImpListIndex_setSize (JNIEnv *env, jclass klass, jlong object, jint size)
{
    ListIndex *listIndex = (ListIndex*) object;

    guint old_size = (guint) listIndex->size;

    listIndex->size = (jint) size;
    list_index_items_changed (listIndex, 0, old_size, size);

}

JNIEXPORT jint JNICALL
Java_ch_bailu_gtk_bridge_ImpListIndex_getIndex (JNIEnv *env, jclass klass, jlong object)
{
    ListIndex *listIndex = (ListIndex*) object;
    return (jint) listIndex->index;
}
    

JNIEXPORT jint JNICALL
Java_ch_bailu_gtk_bridge_ImpListIndex_getSize (JNIEnv *env, jclass klass, jlong object)
{
    ListIndex *listIndex = (ListIndex*) object;
    return (jint) listIndex->size;
}


JNIEXPORT void JNICALL 
Java_ch_bailu_gtk_bridge_ImpListIndex_setIndex (JNIEnv *env, jclass klass, jlong object, jint index)
{
    ListIndex *listIndex = (ListIndex*) object;
    listIndex->index = (int) index;
}
