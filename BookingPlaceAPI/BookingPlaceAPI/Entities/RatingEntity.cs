namespace BookingPlaceAPI.Entities
{
    public class RatingEntity
    {
        public int Id { get; set; }
        public int OrganizationId { get; set; }
        public int UserId { get; set; }
        public string Text { get; set; }
        public double Rating { get; set; }
    }
}
