using BookingPlaceAPI.Enums;
using System.Collections.Generic;
using System.Linq;

namespace BookingPlaceAPI.Models
{
    public class YandexOrganizationInfo
    {
        public List<YandexFeature> Features { get; set; }
    }

    public class YandexFeature
    {
        public YandexFeatureProperties Properties { get; set; }
    }

    public class YandexFeatureProperties
    {
        public string Description { get; set; }
        public string Name { get; set; }
        public YandexCompanyMetaData CompanyMetaData { get; set; }
    }

    public class YandexCompanyMetaData
    {
        public string Name { get; set; }
        public string ShortName { get; set; }
        public List<YandexCompanyPhone> Phones { get; set; }
        public YandexCompanyHours Hours { get; set; }
        public string Url { get; set; }
        public List<YandexCategory> Categories { get; set; }
        public List<YandexCompanyLink> Links { get; set; }
        public string Address { get; set; }

        public string GetCategories()
        {
            if (Categories == null || Categories.Count == 0)
                return string.Empty;

            return string.Join(",", Categories.Select(cat => cat.Name));
        }

        public string GetPhone() => Phones?.FirstOrDefault()?.Formatted;
    }

    public class YandexCompanyLink
    {
        public string Aref { get; set; }
        public string Href { get; set; }

        public SocialNetworkType GetSocialNetworkType()
        {
            switch (Aref)
            {
                case "#vkontakte": return SocialNetworkType.VK;
                case "#facebook": return SocialNetworkType.Facebook;
                case "#instagram": return SocialNetworkType.Instagram;
                default: return SocialNetworkType.None;
            }
        }
    }

    public class YandexCompanyPhone
    {
        public string Formatted { get; set; }
    }

    public class YandexCompanyHours
    {
        public string Text { get; set; }
    }

    public class YandexCategory
    {
        public string Name { get; set; }
    }
}
