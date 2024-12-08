<template>
<div @load="logout">

</div>
</template>

<script>
  export default {
    name: "Logout",
    methods: {
      async deletePoints() {
        try {
          // Send a POST request to delete points
          await this.$axios.post('/api/points/delete');
          console.log('Points deleted successfully');
        } catch (error) {
          console.error('Error deleting points:', error.response ? error.response.data : error);
        }
      },
      clearJwtCookie() {
        // Clear the "jwt" cookie by setting its expiration date to the past
        document.cookie = 'jwt=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;';
      },
      async logout() {
        // Send the delete request before clearing cookies and redirecting
        await this.deletePoints();

        // Clear the jwt cookie
        this.clearJwtCookie();

        // Redirect to the auth page
        this.$router.push({ name: "auth-page" });
      }
    },
    mounted() {
      this.logout(); // Call logout upon component mount
    }
  };

</script>

<style scoped>

</style>