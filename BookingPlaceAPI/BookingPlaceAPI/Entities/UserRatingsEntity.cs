namespace BookingPlaceAPI.Entities
{
    public class UserRatingsEntity
    {
        public int OrganizationId { get; set; }
        public int UserId { get; set; }
        public string Text { get; set; }
        public double Rating { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Phone { get; set; }
    }
}
