<template>

    <v-card outlined>
        <v-card-title>
            RegenerateI
        </v-card-title>

        <v-card-text>
            <Number label="Id" v-model="value.id" :editMode="editMode"/>
            <Number label="AuthorId" v-model="value.authorId" :editMode="editMode"/>
            <Number label="ScriptId" v-model="value.scriptId" :editMode="editMode"/>
            <String label="Contents" v-model="value.contents" :editMode="editMode"/>
            <String label="Status" v-model="value.status" :editMode="editMode"/>
            <String label="Title" v-model="value.title" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="regenerateI"
            >
                RegenerateI
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'RegenerateICommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.id = 0;
        this.value.authorId = 0;
        this.value.scriptId = 0;
        this.value.contents = '';
        this.value.status = '';
        this.value.title = '';
    },
    watch: {
    },
    methods: {
        regenerateI() {
            this.$emit('regenerateI', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

