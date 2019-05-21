using BookingPlaceAPI.Models;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Services
{
    public interface IYandexSearchService
    {
        Task<YandexCompanyMetaData> SearchOrganization(string organizationName);
    }
}
